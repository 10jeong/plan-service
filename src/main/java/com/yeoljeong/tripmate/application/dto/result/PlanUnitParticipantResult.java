package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record PlanUnitParticipantResult(
    UUID id,
    UUID userId,
    String name,
    ParticipationRole participationRole,
    ParticipationStatus participationStatus
) implements Serializable {

  public static PlanUnitParticipantResult from(PlanParticipantDetail participant) {
    return new PlanUnitParticipantResult(
        participant.id(),
        participant.userId(),
        participant.userName(),
        participant.role(),
        participant.status());
  }

  public static List<PlanUnitParticipantResult> from(List<PlanParticipantDetail> unitParticipants) {
    return unitParticipants.stream()
        .map(PlanUnitParticipantResult::from)
        .toList();
  }
}
