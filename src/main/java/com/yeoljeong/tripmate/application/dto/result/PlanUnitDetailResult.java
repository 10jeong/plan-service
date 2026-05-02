package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import com.yeoljeong.tripmate.presentation.dto.response.PlanUnitDetail;
import java.math.BigDecimal;
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
    UUID productId,
    BigDecimal price,
    int currentCount,
    int maxCount,
    boolean isConfirmed,
    List<PlanUnitParticipantResult> participants
) {

  public static PlanUnitDetailResult from(PlanUnit unit, List<PlanParticipation> unitParticipants) {
    return new PlanUnitDetailResult(
        unit.getId(),
        unit.getDay(),
        unit.getOrderIndex(),
        unit.getTitle(),
        unit.getDescription(),
        unit.getUnitTimeRange().getStartTime(),
        unit.getUnitTimeRange().getEndTime(),
        unit.getProductId(),
        unit.getPrice(),
        unit.getParticipantCount().getCurrentCount(),
        unit.getParticipantCount().getMaxCount(),
        unit.isConfirmed(),
        PlanUnitParticipantResult.from(unitParticipants));
  }
}
