package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.application.dto.external.ProductSummaryData;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record PlanUnitDetailResult(
    UUID id,
    int day,
    int orderIndex,
    String title,
    String description,
    LocalTime startTime,
    LocalTime endTime,
    int currentCount,
    int maxCount,
    boolean isConfirmed,
    ProductSummaryData productSummary,
    List<PlanUnitParticipantResult> participants
) implements Serializable {

  public static PlanUnitDetailResult from(PlanUnit unit, ProductSummaryData productSummaryData, List<PlanParticipantDetail> unitParticipants) {
    return new PlanUnitDetailResult(
        unit.getId(),
        unit.getDay(),
        unit.getOrderIndex(),
        unit.getTitle(),
        unit.getDescription(),
        unit.getUnitTimeRange().getStartTime(),
        unit.getUnitTimeRange().getEndTime(),
        unit.getParticipantCount().getCurrentCount(),
        unit.getParticipantCount().getMaxCount(),
        unit.isConfirmed(),
        productSummaryData,
        PlanUnitParticipantResult.from(unitParticipants));
  }
}
