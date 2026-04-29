package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.enums.ParticipantStatus;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateParticipantStatusResult(
    UUID planParticipantId,
    String title,
    ParticipantStatus status,
    LocalDateTime updatedAt,
    UUID updatedBy
) {


  public static UpdateParticipantStatusResult from(PlanParticipation savedPlanParticipation) {
    return new UpdateParticipantStatusResult(
        savedPlanParticipation.getId(),
        savedPlanParticipation.getPlanUnit().getTitle(),
        savedPlanParticipation.getParticipantStatus(),
        savedPlanParticipation.getUpdatedAt(),
        savedPlanParticipation.getUpdatedBy()
    );
  }
}
