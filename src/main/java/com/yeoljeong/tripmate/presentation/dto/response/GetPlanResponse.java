package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import java.time.LocalDate;
import java.util.UUID;

public record GetPlanResponse(
    UUID PlanId,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    RecruitStatus recruitStatus,
    String imageUrl
) {

  public static GetPlanResponse from(Plan plan) {
    return new GetPlanResponse(
        plan.getId(),
        plan.getTitle(),
        plan.getDescription(),
        plan.getTravelPeriod().getStartDate(),
        plan.getTravelPeriod().getEndDate(),
        plan.getRecruitStatus(),
        plan.getImageUrl()
    );
  }
}
