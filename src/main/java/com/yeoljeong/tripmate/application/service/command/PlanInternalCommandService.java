package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.application.port.PlanEvents;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
import com.yeoljeong.tripmate.event.PaymentCompletedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import com.yeoljeong.tripmate.exception.BusinessException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanInternalCommandService {

  private final PlanParticipationRepository planParticipationRepository;
  private final PlanUnitRepository planUnitRepository;
  private final PlanEvents events;

  public FindParticipationStatusResult findParticipationStatusByPlanUnitIdAndUserId(UUID planUnitId, UUID userId) {

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(planUnitId, userId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    return new FindParticipationStatusResult(planUnitId, userId,
        participation.getParticipationStatus());
  }

  /*
  * 주문 생성시, 참여인원 증가 및 상태 변경(APPROVED -> RESERVED)
  * */
  public void addPlanUnitParticipant(OrderCreatedEvent event) {

    // todo: 멱등성 처리
    PlanUnit planUnit = planUnitRepository.findById(event.planUnitId())
            .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));

    PlanParticipation planParticipation = planParticipationRepository.findByPlanUnitAndUserId(
            planUnit, event.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    // 침야 싱테 검증
    planParticipation.validateApprovalStatus();

    // 참여 인원 검증
    planUnit.validatePositive(event.quantity());

    // 참여 인원 증가 (atomic update)
    int updated = planUnitRepository.addParticipantCount(planUnit.getId(), event.quantity());

    if (updated == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_EXCEED);
    }

    // 참여 상태 변경
    planParticipation.confirmParticipation();

    // 이벤트 발행
    events.planUnitAddParticipant(new PlanUnitParticipantAddedEvent(event.eventId(), event.productId(), event.scheduleId(), event.quantity()));

  }

  /*
  * 결제 완료시, 참여 상태 변경 (RESERVED -> CONFIRMED)
  * */
  public void updateParticipantStatus(PaymentCompletedEvent event) {
    // todo: event에 planUnitId가 들어올때 수정
    UUID planUnitId = UUID.randomUUID();
    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(planUnitId, event.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));
    participation.validatePlanParticipationStatus(participation.getParticipationStatus());

    // 상태 변경 (RESERVED -> CONFIRMED)
    int updated = planParticipationRepository.updateStatus(
        participation.getPlanUnit().getId(),
        participation.getParticipationStatus(),
        ParticipationStatus.CONFIRMED);

    if (updated == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID);
    }
  }

}
