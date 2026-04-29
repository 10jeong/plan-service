package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.ParticipatePlanResult;
import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipatePlanResponse(
    UUID planParticipantId,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static ParticipatePlanResponse from(ParticipatePlanResult result) {
    return new ParticipatePlanResponse(
        result.planParticipantId(),
        result.updatedAt(),
        result.updatedBy());
  }
}
