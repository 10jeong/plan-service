package com.yeoljeong.tripmate.application.service.query;

import com.yeoljeong.tripmate.application.dto.result.PlanUnitDetailResult;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.plan.Plan;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanRepository;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.exception.BusinessException;
import com.yeoljeong.tripmate.application.dto.result.GetPlanDetailResult;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanQueryService {

  private final PlanRepository planRepository;
  private final PlanUnitRepository planUnitRepository;
  private final PlanParticipationRepository participationRepository;

  public GetPlanDetailResult getPlanDetail(UUID planId) {
    Plan plan = planRepository.findById(planId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_NOT_FOUND));

    List<PlanUnit> planUnit = planUnitRepository.findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(plan);
    List<PlanParticipation> planParticipation = participationRepository.findAllByPlanUnitIn(planUnit);

    // 참여자 그룹핑
    Map<PlanUnit, List<PlanParticipation>> participationMap= planParticipation.stream()
        .collect(Collectors.groupingBy(PlanParticipation::getPlanUnit));

    List<PlanUnitDetailResult> planUnitResult =
        planUnit.stream()
            .map(unit ->
              PlanUnitDetailResult.from(unit, participationMap.getOrDefault(unit, List.of())
              )).toList();
    return GetPlanDetailResult.from(plan, planUnitResult);

  }
}
