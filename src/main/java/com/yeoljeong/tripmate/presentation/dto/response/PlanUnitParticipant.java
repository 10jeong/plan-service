package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.PlanUnitParticipantResult;
import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.util.List;
import java.util.UUID;

public record PlanUnitParticipant(
    UUID id,
    ParticipationRole participationRole,
    ParticipationStatus participationStatus
) {

  public static PlanUnitParticipant from(PlanUnitParticipantResult participant) {
    return new PlanUnitParticipant(
        participant.id(),
        participant.participationRole(),
        participant.participationStatus()
    );
  }

  public static List<PlanUnitParticipant> from(List<PlanUnitParticipantResult> participants) {
    return participants.stream()
        .map(PlanUnitParticipant::from)
        .toList();
  }
}
