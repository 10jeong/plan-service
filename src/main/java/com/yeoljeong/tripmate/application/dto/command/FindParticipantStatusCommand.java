package com.yeoljeong.tripmate.application.dto.command;

import java.util.UUID;

public record FindParticipantStatusCommand(
    UUID planUnitId,
    UUID userId) {

}
