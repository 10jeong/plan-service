package com.yeoljeong.tripmate.application.dto.external;

import java.util.UUID;

public record OrderPlanUnitData(
    UUID planUnitId,
    UUID orderId
) {

}
