package com.yeoljeong.tripmate.domain.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductData(
    UUID productId,
    @JsonProperty("scheduleId")
    UUID productScheduleId,
    String productName,
    String country,
    String state,
    String city,
    BigDecimal price
) {

}
