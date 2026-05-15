package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReceivedParticipationRequestsResponse(
    UUID planId,
    String planTitle,
    LocalDate startDate,
    LocalDate endDate,
    RecruitStatus recruitStatus,
    List<PlanUnitParticipationSummary> planUnits
) {

  public static ReceivedParticipationRequestsResponse from(Plan plan, List<PlanUnitParticipationSummary> planUnits) {
    return new ReceivedParticipationRequestsResponse(
        plan.getId(),
        plan.getTitle(),
        plan.getTravelPeriod().getStartDate(),
        plan.getTravelPeriod().getEndDate(),
        plan.getRecruitStatus(),
        planUnits
    );
  }
}
