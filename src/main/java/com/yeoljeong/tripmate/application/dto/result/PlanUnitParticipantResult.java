package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.util.List;
import java.util.UUID;

public record PlanUnitParticipantResult(
    UUID id,
    ParticipationRole participationRole,
    ParticipationStatus participationStatus
) {

  public static PlanUnitParticipantResult from(PlanParticipation participant) {
    return new PlanUnitParticipantResult(
        participant.getId(),
        participant.getParticipationRole(),
        participant.getParticipationStatus());
  }

  public static List<PlanUnitParticipantResult> from(List<PlanParticipation> unitParticipants) {
    return unitParticipants.stream()
        .map(PlanUnitParticipantResult::from)
        .toList();
  }
}
