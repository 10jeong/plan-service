package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;


public interface PlanEvents {

  void planUnitAddParticipant(PlanUnitParticipantAddedEvent planUnitParticipantAddedEvent);

  void planUnitConfirmed(PlanUnitConfirmedEvent planUnitConfirmedEvent);
}
