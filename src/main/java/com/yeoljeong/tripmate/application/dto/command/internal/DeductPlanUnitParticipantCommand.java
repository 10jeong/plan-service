package com.yeoljeong.tripmate.application.dto.command.internal;

import com.yeoljeong.tripmate.event.ProductStockDeductFailedEvent;
import java.util.UUID;

public record DeductPlanUnitParticipantCommand(
    UUID eventId,
    UUID orderId,
    UUID planUnitId,
    UUID userId,
    int quantity
) {

  public static DeductPlanUnitParticipantCommand from(ProductStockDeductFailedEvent event) {
    return new DeductPlanUnitParticipantCommand(
      event.eventId(),
      event.orderId(),
      event.planUnitId(),
      event.userId(),
      event.quantity()
    );
  }
}
