package com.yeoljeong.tripmate.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConfirmPlanUnitResult(
    UUID planUnitId,
    String title,
    boolean isConfirmed,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static ConfirmPlanUnitResult from(UUID planUnitId, String title, boolean isConfirmed, LocalDateTime updatedAt, UUID updatedBy) {
    return new ConfirmPlanUnitResult(planUnitId, title, isConfirmed, updatedAt, updatedBy);
  }
}
