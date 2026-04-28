package com.yeoljeong.tripmate.application;

import com.yeoljeong.tripmate.application.dto.command.CreatePlanCommand;
import com.yeoljeong.tripmate.application.dto.command.CreatePlanUnitCommand;
import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanRepository;
import com.yeoljeong.tripmate.domain.model.plan.Plan;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.exception.BusinessException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

  private final PlanRepository planRepository;
  private final PlanUnitRepository planUnitRepository;
  private final PlanParticipationRepository planParticipationRepository;

  public CreatePlanResult createPlans(CreatePlanCommand command) {

    Plan plan = Plan.builder()
        .title(command.getTitle())
        .description(command.getDescription())
        .startDate(command.getStartDate())
        .endDate(command.getEndDate())
        .planType(command.getPlanType())
        .build();
    Plan savedPlan = planRepository.save(plan);


    // 동일한 [단위 일정ID, 일차, 순서] 존재 x
    validateDuplicateDayAndOrder(command.getPlanUnit());

    // 단위 일정 시간 범위가 겹치지 않는지 검증
    validateUnitTimeRangeNotOverlap(command.getPlanUnit());

    List<PlanUnit> planUnit = command.getPlanUnit().stream()
        .map(unitCommand -> PlanUnit.builder()
            .day(unitCommand.getDay())
            .orderIndex(unitCommand.getOrderIndex())
            .title(unitCommand.getTitle())
            .description(unitCommand.getDescription())
            .startTime(unitCommand.getStartTime())
            .endTime(unitCommand.getEndTime())
            .price(unitCommand.getPrice())
            .maxCount(unitCommand.getMaxCount())
            .plan(plan)
            .productId(unitCommand.getProductId())
            .build())
        .toList();

    planUnitRepository.saveAll(planUnit);

    List<PlanParticipation> planParticipation = planUnit.stream()
        .map(unit -> PlanParticipation.createHost(command.getHostId(),unit))
        .toList();

    planParticipationRepository.saveAll(planParticipation);

    return CreatePlanResult.from(savedPlan);

  }

  private void validateUnitTimeRangeNotOverlap(List<CreatePlanUnitCommand> planUnit) {
    // 일차의 순서대로 정렬
    List<CreatePlanUnitCommand> sortedPlanUnits = planUnit.stream()
        .sorted(
            Comparator.comparingInt(CreatePlanUnitCommand::getDay)
                .thenComparingInt(CreatePlanUnitCommand::getOrderIndex)
        )
        .toList();

    for(int i=0; i<sortedPlanUnits.size()-1; i++) {
      CreatePlanUnitCommand previous = sortedPlanUnits.get(i);
      CreatePlanUnitCommand current = sortedPlanUnits.get(i+1);
      int previousDay = previous.getDay();
      int currentDay = current.getDay();

      // 일차가 다르다면 비교 x
      if (previousDay != currentDay) continue;

      // 일차가 같다면 이전 순서의 종료시간과 이후 순서의 시작시간 비교
      if (previous.getEndTime().isAfter(current.getStartTime())) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_OVERLAP);
      }
    }
  }

  private void validateDuplicateDayAndOrder(List<CreatePlanUnitCommand> planUnit) {
    Set<String> keys = new HashSet<>();

    for (CreatePlanUnitCommand unit : planUnit) {
      String key = unit.getDay() + ", " + unit.getOrderIndex();

      if (!keys.add(key)) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_DUPLICATED_ORDER);
      }


    }
  }
}
