package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import java.util.UUID;

public record FindParticipationStatusResponse(
    UUID planUnitId,
    UUID userId,
    String status
) {

  public static FindParticipationStatusResponse from(FindParticipationStatusResult result) {
    return new FindParticipationStatusResponse(result.planUnitId(), result.userId(),
        result.status().toString());
  }
}
