package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.enums.PlanCreationType;
import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MyParticipationRequestsResponse(
    UUID planId,
    String planTitle,
    LocalDate startDate,
    LocalDate endDate,
    PlanCreationType planType,
    RecruitStatus recruitStatus,
    List<ParticipationSummary> participations
) {

  public static MyParticipationRequestsResponse from(Plan plan, List<ParticipationSummary> participations) {
    return new MyParticipationRequestsResponse(
        plan.getId(),
        plan.getTitle(),
        plan.getTravelPeriod().getStartDate(),
        plan.getTravelPeriod().getEndDate(),
        plan.getPlanType(),
        plan.getRecruitStatus(),
        participations
    );
  }
}
