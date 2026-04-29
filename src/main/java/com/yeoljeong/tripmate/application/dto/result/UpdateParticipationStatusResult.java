package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateParticipationStatusResult(
    UUID planParticipationId,
    String title,
    ParticipationStatus status,
    LocalDateTime updatedAt,
    UUID updatedBy
) {


  public static UpdateParticipationStatusResult from(PlanParticipation savedPlanParticipation) {
    return new UpdateParticipationStatusResult(
        savedPlanParticipation.getId(),
        savedPlanParticipation.getPlanUnit().getTitle(),
        savedPlanParticipation.getParticipationStatus(),
        savedPlanParticipation.getUpdatedAt(),
        savedPlanParticipation.getUpdatedBy()
    );
  }
}
