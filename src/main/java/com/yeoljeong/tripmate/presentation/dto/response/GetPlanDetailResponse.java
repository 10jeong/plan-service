package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.GetPlanDetailResult;
import com.yeoljeong.tripmate.domain.enums.PlanCreationType;
import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetPlanDetailResponse(
    UUID id,
    String title,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    PlanCreationType planCreationType,
    RecruitStatus recruitStatus,
    List<PlanUnitDetail> planUnits


) {

  public static GetPlanDetailResponse from(GetPlanDetailResult result) {
    return new GetPlanDetailResponse(
        result.id(),
        result.title(),
        result.description(),
        result.startDate(),
        result.endDate(),
        result.planCreationType(),
        result.recruitStatus(),
        PlanUnitDetail.from(result.planUnits())
    );
  }
}
