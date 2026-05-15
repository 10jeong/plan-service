package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import java.util.UUID;

public record ReceivedParticipationSummary(
    UUID participationId,
    UUID userId,
    String name,
    ParticipationStatus status
) {

  public static ReceivedParticipationSummary from(PlanParticipation participation,
      UserData userData) {
    return new ReceivedParticipationSummary(
        participation.getId(),
        userData.userId(),
        userData.userName(),
        participation.getParticipationStatus());
  }
}
