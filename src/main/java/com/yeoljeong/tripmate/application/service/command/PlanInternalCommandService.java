package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.command.internal.DeductPlanUnitParticipantByProductCommand;
import com.yeoljeong.tripmate.application.dto.external.OrderPlanUnitData;
import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.application.port.OrderReader;
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
  private final OrderReader orderReader;

  public FindParticipationStatusResult findParticipationStatusByPlanUnitIdAndUserId(UUID planUnitId, UUID userId) {

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(planUnitId, userId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    return new FindParticipationStatusResult(planUnitId, userId,
        participation.getParticipationStatus());
  }

  /*
   * 주문 생성시, 참여인원 증가 및 상태 변경(APPROVED -> RESERVED) + 이벤트 발행
   * */
  public void addPlanUnitParticipant(OrderCreatedEvent event) {

    // todo: 멱등성 처리
    PlanUnit planUnit = planUnitRepository.findById(event.planUnitId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));

    PlanParticipation planParticipation = planParticipationRepository.findByPlanUnit_IdAndUserId(
            planUnit.getId(), event.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    // 참여 싱테 검증
    planParticipation.validatePlanParticipationStatus(ParticipationStatus.RESERVED);

    // 참여 인원 검증
    planUnit.validatePositive(event.quantity());

    // 참여 인원 증가 (atomic update)
    int updatedCount = planUnitRepository.addParticipantCount(planUnit.getId(), event.quantity());

    if (updatedCount == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_EXCEED);
    }

    // 참여 상태 변경
    int updatedStatus = planParticipationRepository.updateStatus(planParticipation.getId(),
        planParticipation.getParticipationStatus(), ParticipationStatus.RESERVED);

    if (updatedStatus == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID);
    }

    // 이벤트 발행
    events.planUnitAddParticipant(
        new PlanUnitParticipantAddedEvent(
            UUID.randomUUID(),
            event.orderId(),
            event.productId(),
            event.scheduleId(),
            event.planUnitId(),
            event.userId(),
            event.quantity()
        )
    );

  }

  /*
  * 결제 완료시, 참여 상태 변경 (RESERVED -> CONFIRMED)
  * */
  public void updateParticipantStatus(PaymentCompletedEvent event) {

    OrderPlanUnitData orderPlanUnitData = orderReader.getPlanUnitId(event.orderId());
    if (orderPlanUnitData == null) {
      throw new BusinessException(PlanErrorCode.ORDER_PLAN_UNIT_NOT_FOUND);
    }

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(orderPlanUnitData.planUnitId(), event.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    participation.validatePlanParticipationStatus(ParticipationStatus.CONFIRMED);

    // 상태 변경 (RESERVED -> CONFIRMED)
    int updated = planParticipationRepository.updateStatus(
        participation.getPlanUnit().getId(),
        participation.getParticipationStatus(),
        ParticipationStatus.CONFIRMED);

    if (updated == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID);
    }
  }

  /*
   * 상품 재고 차감 실패시, 참여인원 감소 및 참여 상태 변경(RESERVED -> APPROVED) + 이벤트 발행
   * */
  public void deductPlanUnitParticipantByProduct(DeductPlanUnitParticipantByProductCommand command) {
    // todo: 멱등성 처리
    PlanUnit planUnit = planUnitRepository.findById(command.planUnitId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(
            command.planUnitId(), command.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    // 참여 상태 변경 (RESERVED -> APPROVED)
    int updatedStatus = planParticipationRepository.updateStatus(participation.getId(),
        ParticipationStatus.RESERVED, ParticipationStatus.APPROVED);

    if (updatedStatus == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID);
    }

    // 참여 현재인원 감소
    planUnit.validatePositive(command.quantity());
    int updatedCount = planUnitRepository.deductParticipantCount(command.planUnitId(), command.quantity());

    if (updatedCount == 0) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_UPDATE_QUANTITY_INVALID);
    }

    // 이벤트 발행
    events.deductPlanUnitParticipantByProduct(UUID.randomUUID(), command.orderId());
  }
}
