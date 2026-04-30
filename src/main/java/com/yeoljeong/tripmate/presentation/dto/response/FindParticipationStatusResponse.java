package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.util.UUID;

public record FindParticipationStatusResponse(
    UUID planUnitId,
    UUID userId,
    ParticipationStatus status
) {

  public static FindParticipationStatusResponse from(FindParticipationStatusResult result) {
    return new FindParticipationStatusResponse(result.planUnitId(), result.userId(),
        result.status());
  }
}
