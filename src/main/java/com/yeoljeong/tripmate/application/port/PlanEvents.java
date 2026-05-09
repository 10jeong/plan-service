package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import java.util.UUID;


public interface PlanEvents {

  void planUnitAddParticipant(PlanUnitParticipantAddedEvent planUnitParticipantAddedEvent);

  void planUnitConfirmed(PlanUnitConfirmedEvent planUnitConfirmedEvent);

  void deductPlanUnitParticipant(UUID eventId, UUID orderId);
}
