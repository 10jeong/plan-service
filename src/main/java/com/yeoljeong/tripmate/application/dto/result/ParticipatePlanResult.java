package com.yeoljeong.tripmate.application.dto.result;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipatePlanResult(
    UUID planParticipationId,
    String planUnitTitle,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static ParticipatePlanResult from(UUID id, String title, LocalDateTime updatedAt, UUID updatedBy) {
    return new ParticipatePlanResult(id, title,updatedAt, updatedBy);
  }
}
