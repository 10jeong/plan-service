package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.PlanCreationType;
import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetPlanDetailResult(
    UUID id,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    PlanCreationType planCreationType,
    RecruitStatus recruitStatus,
    List<PlanUnitDetailResult> planUnits
) implements Serializable {

  public static GetPlanDetailResult from(Plan plan, List<PlanUnitDetailResult> planUnitResult) {
    return new GetPlanDetailResult(
        plan.getId(),
        plan.getTitle(),
        plan.getDescription(),
        plan.getTravelPeriod().getStartDate(),
        plan.getTravelPeriod().getEndDate(),
        plan.getPlanType(),
        plan.getRecruitStatus(),
        planUnitResult
    );
  }
}
