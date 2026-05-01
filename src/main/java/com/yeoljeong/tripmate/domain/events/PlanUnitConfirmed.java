package com.yeoljeong.tripmate.domain.events;

import java.util.List;
import java.util.UUID;

public record PlanUnitConfirmed(
    String eventHash,
    UUID planUnitId,
    String planTitle,
    List<UUID> receivers
) {

}
