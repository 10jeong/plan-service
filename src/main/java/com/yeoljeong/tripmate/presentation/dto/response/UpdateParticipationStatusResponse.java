package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.UpdateParticipationStatusResult;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateParticipationStatusResponse(
    UUID planParticipationId,
    String title,
    ParticipationStatus status,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static UpdateParticipationStatusResponse from(UpdateParticipationStatusResult result) {
    return new UpdateParticipationStatusResponse(
        result.planParticipationId(),
        result.title(),
        result.status(),
        result.updatedAt(),
        result.updatedBy());
  }
}
