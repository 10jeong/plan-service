package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.result.PlanUnitDetailResult;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record PlanUnitDetail(
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
    PlanProductSummary product,
    List<PlanUnitParticipant> participants
) {

  public static List<PlanUnitDetail> from(List<PlanUnitDetailResult> planUnitDetailResults) {
    return planUnitDetailResults.stream()
        .map(result -> new PlanUnitDetail(
            result.id(),
            result.day(),
            result.orderIndex(),
            result.title(),
            result.description(),
            result.startTime(),
            result.endTime(),
            result.currentCount(),
            result.maxCount(),
            result.isConfirmed(),
            PlanProductSummary.from(result.productSummary()),
            PlanUnitParticipant.from(result.participants())
        )).toList();
  }
}
