package com.yeoljeong.tripmate.application.dto.command.internal;

import com.yeoljeong.tripmate.event.OrderSchedulerCancelledEvent;
import java.util.UUID;

public record DeductPlanUnitParticipantByOrderCommand(
    UUID eventId,
    UUID planUnitId,
    UUID userId,
    UUID productId,
    int quantity
) {

  public static DeductPlanUnitParticipantByOrderCommand from(OrderSchedulerCancelledEvent event) {
    return new DeductPlanUnitParticipantByOrderCommand(
        event.eventId(),
        event.planUnitId(),
        event.userId(),
        event.productId(),
        event.quantity()
    );
  }
}
