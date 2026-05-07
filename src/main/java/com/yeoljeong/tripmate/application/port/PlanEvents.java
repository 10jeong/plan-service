package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import java.util.List;
import java.util.UUID;

public interface PlanEvents {

  void planUnitAddParticipant(PlanUnitParticipantAddedEvent planUnitParticipantAddedEvent);

  void planUnitConfirmed(String planUnit, UUID planUnitId, String title, List<UUID> receivers);
}
