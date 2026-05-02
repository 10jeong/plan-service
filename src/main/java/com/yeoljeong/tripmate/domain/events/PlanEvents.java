package com.yeoljeong.tripmate.domain.events;

import java.util.List;
import java.util.UUID;

public interface PlanEvents {
  void planUnitConfirmed(String eventHash, UUID planUnitId, String title, List<UUID> receivers);

  void planUnitAddParticipant(UUID eventId, UUID productId, UUID scheduleId, int quantity);
}
