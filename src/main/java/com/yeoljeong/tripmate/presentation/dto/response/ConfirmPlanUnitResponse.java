package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.ConfirmPlanUnitResult;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConfirmPlanUnitResponse(
    UUID planUnitId,
    String title,
    Boolean isConfirmed,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static ConfirmPlanUnitResponse from(ConfirmPlanUnitResult result) {
    return new ConfirmPlanUnitResponse(
        result.planUnitId(),
        result.title(),
        result.isConfirmed(),
        result.updatedAt(),
        result.updatedBy());
  }
}
