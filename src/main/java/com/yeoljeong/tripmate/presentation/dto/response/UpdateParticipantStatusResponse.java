package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.UpdateParticipantStatusResult;
import com.yeoljeong.tripmate.domain.enums.ParticipantStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateParticipantStatusResponse(
    UUID planParticipantId,
    String title,
    ParticipantStatus status,
    LocalDateTime updatedAt,
    UUID updatedBy
) {

  public static UpdateParticipantStatusResponse from(UpdateParticipantStatusResult result) {
    return new UpdateParticipantStatusResponse(
        result.planParticipantId(),
        result.title(),
        result.status(),
        result.updatedAt(),
        result.updatedBy());
  }
}
