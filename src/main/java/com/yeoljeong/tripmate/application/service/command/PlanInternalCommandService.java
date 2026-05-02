package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
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

    planUnit.addPlanUnitParticipant(event.quantity());
  }
}
