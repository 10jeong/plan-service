package com.yeoljeong.tripmate.application.dto.command;

import com.yeoljeong.tripmate.domain.enums.ParticipantStatus;
import java.util.UUID;

public record UpdateParticipantStatusCommand(
    UUID userId,
    UUID planId,
    UUID planUnitId,
    UUID participationId,
    ParticipantStatus status
) {

}
