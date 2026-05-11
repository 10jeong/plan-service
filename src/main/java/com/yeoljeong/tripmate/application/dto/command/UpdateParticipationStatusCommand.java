package com.yeoljeong.tripmate.application.dto.command;

import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.util.UUID;

public record UpdateParticipationStatusCommand(
    UUID userId,
    UUID planId,
    UUID planUnitId,
    UUID participationId,
    ParticipationStatus status
) {

}
