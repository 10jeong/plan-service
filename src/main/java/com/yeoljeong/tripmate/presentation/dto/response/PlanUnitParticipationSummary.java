package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record PlanUnitParticipationSummary(
    UUID planUnitId,
    String title,
    int day,
    LocalTime startTime,
    LocalTime endTime,
    int maxCount,
    int currentCount,
    boolean isConfirmed,
    List<ReceivedParticipationSummary> applicants
) {

  public static PlanUnitParticipationSummary from(List<ReceivedParticipationSummary> applicants, PlanUnit planUnit) {
    return new PlanUnitParticipationSummary(
        planUnit.getId(),
        planUnit.getTitle(),
        planUnit.getDay(),
        planUnit.getUnitTimeRange().getStartTime(),
        planUnit.getUnitTimeRange().getEndTime(),
        planUnit.getParticipantCount().getMaxCount(),
        planUnit.getParticipantCount().getCurrentCount(),
        planUnit.isConfirmed(),
        applicants
    );
  }
}
