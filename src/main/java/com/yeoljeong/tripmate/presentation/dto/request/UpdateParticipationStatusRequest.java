package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.application.dto.command.UpdateParticipationStatusCommand;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateParticipationStatusRequest(
    @NotNull ParticipationStatus status
) {

  public UpdateParticipationStatusCommand toCommand(
      UUID userId,
      UUID planId,
      UUID planUnitId,
      UUID participationId
      ) {
    return new UpdateParticipationStatusCommand(userId, planId, planUnitId, participationId, status);
  }
}
