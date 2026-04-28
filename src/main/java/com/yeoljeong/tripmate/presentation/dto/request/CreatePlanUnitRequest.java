package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.application.dto.command.CreatePlanUnitCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record CreatePlanUnitRequest(
  @Min(1) int day,
  @Min(1) int orderIndex,
  @NotBlank String title,
  @NotBlank String description,
  @NotNull LocalTime startTime,
  @NotNull LocalTime endTime,
  @NotNull @PositiveOrZero BigDecimal price,
  @Min(1) int maxCount,
  @NotNull UUID productId
) {

  public CreatePlanUnitCommand toCommand() {
    return CreatePlanUnitCommand.builder()
        .day(day)
        .orderIndex(orderIndex)
        .title(title)
        .description(description)
        .startTime(startTime)
        .endTime(endTime)
        .price(price)
        .maxCount(maxCount)
        .productId(productId)
        .build();
  }
}
