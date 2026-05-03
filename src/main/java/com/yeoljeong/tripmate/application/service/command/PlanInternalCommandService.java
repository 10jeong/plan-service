package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import com.yeoljeong.tripmate.exception.BusinessException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanInternalCommandService {

  private final PlanParticipationRepository planParticipationRepository;
  private final PlanUnitRepository planUnitRepository;
  private final ApplicationEventPublisher publisher;

  public FindParticipationStatusResult findParticipationStatusByPlanUnitIdAndUserId(UUID planUnitId, UUID userId) {

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(planUnitId, userId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    return new FindParticipationStatusResult(planUnitId, userId,
        participation.getParticipationStatus());
  }

  public void addPlanUnitParticipant(OrderCreatedEvent event) {

    // todo: 동시에 read 한다면 업데이트 유실이 발생할 수 있음.
    //  -> 낙관적 락(+재시도) or 비관적 락 or 원자 update 쿼리 보호 필요
    PlanUnit planUnit = planUnitRepository.findById(event.planUnitId())
            .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));

    PlanParticipation planParticipation = planParticipationRepository.findByPlanUnitAndUserId(
            planUnit, event.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    planParticipation.confirmParticipation();
    planUnit.addPlanUnitParticipant(event.quantity());

    publisher.publishEvent(new PlanUnitParticipantAddedEvent(event.eventId(), event.productId(), event.scheduleId(), event.quantity()));
  }
}
