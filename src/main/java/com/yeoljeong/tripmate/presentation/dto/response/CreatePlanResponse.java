package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePlanResponse(
  UUID planId,
  String title,
  LocalDateTime createdAt,
  UUID createdBy
) {

  public static CreatePlanResponse from(CreatePlanResult result) {
    return new CreatePlanResponse(
        result.getPlanId(),
        result.getTitle(),
        result.getCreatedAt(),
        result.getCreatedBy());
  }
}
