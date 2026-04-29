package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.application.dto.command.UpdateParticipantStatusCommand;
import com.yeoljeong.tripmate.domain.enums.ParticipantStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateParticipantStatusRequest(
    @NotNull ParticipantStatus status
) {

  public UpdateParticipantStatusCommand toCommand(
      UUID userId,
      UUID planId,
      UUID planUnitId,
      UUID participationId
      ) {
    return new UpdateParticipantStatusCommand(userId, planId, planUnitId, participationId, status);
  }
}
