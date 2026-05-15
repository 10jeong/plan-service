package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.time.LocalTime;
import java.util.UUID;

public record ParticipationSummary(
    UUID participationId,
    UUID planUnitId,
    String unitTitle,
    int day,
    LocalTime startTime,
    LocalTime endTime,
    ParticipationStatus staus
) {

  public static ParticipationSummary from(PlanUnit planUnit, PlanParticipation participation) {
    return new ParticipationSummary(
        participation.getId(),
        planUnit.getId(),
        planUnit.getTitle(),
        planUnit.getDay(),
        planUnit.getUnitTimeRange().getStartTime(),
        planUnit.getUnitTimeRange().getEndTime(),
        participation.getParticipationStatus()
    );
  }
}
