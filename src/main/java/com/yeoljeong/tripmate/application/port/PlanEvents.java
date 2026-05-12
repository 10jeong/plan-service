package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import java.util.UUID;


public interface PlanEvents {

  void planUnitAddParticipant(PlanUnitParticipantAddedEvent planUnitParticipantAddedEvent);

  void planUnitConfirmed(PlanUnitConfirmedEvent planUnitConfirmedEvent);

  void deductPlanUnitParticipantByProduct(UUID eventId, UUID orderId);

  void addPlanUnitParticipantFailed(UUID eventId, UUID orderId);

  void deductPlanUnitParticipantFailedByProduct(UUID eventId, UUID orderId);

  void planUnitParticipationQuit(UUID eventId, UUID userId, UUID planUnitId, String reason);

  void deductPlanUnitParticipantByOrder(UUID eventId, UUID productId, UUID productScheduleId, int quantity);
}
