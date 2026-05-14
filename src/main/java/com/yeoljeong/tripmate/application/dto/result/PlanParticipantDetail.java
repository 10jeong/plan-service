package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import java.util.UUID;

public record PlanParticipantDetail(
    UUID id,
    UUID userId,
    String userName,
    ParticipationRole role,
    ParticipationStatus status
) {

  public static PlanParticipantDetail from(UserData user, PlanParticipation planParticipation) {
    return new PlanParticipantDetail(
        planParticipation.getId(),
        planParticipation.getUserId(),
        user.userName(),
        planParticipation.getParticipationRole(),
        planParticipation.getParticipationStatus());
  }
}
