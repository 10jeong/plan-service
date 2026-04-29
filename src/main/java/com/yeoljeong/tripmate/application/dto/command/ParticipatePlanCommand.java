package com.yeoljeong.tripmate.application.dto.command;

import java.util.UUID;

public record ParticipatePlanCommand(
    UUID guestId,
    UUID planId,
    UUID planUnitId
) {

}
