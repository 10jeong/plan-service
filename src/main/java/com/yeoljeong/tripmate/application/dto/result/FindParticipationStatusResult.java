package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.util.UUID;

public record FindParticipationStatusResult(
    UUID planUnitId,
    UUID userId,
    ParticipationStatus status
) {

}
