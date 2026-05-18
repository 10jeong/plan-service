package com.yeoljeong.tripmate.application.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductSummaryData(
    UUID productId,
    @JsonProperty("scheduleId")
    UUID productScheduleId,
    String productName,
    BigDecimal price
) implements Serializable {

}
