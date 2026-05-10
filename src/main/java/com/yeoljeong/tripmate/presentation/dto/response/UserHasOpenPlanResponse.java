package com.yeoljeong.tripmate.presentation.dto.response;

public record UserHasOpenPlanResponse(
    boolean hasActiveData
) {

  public static UserHasOpenPlanResponse from(boolean existsOpenPlan) {
    return new UserHasOpenPlanResponse(existsOpenPlan);
  }
}
