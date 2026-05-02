package com.yeoljeong.tripmate.domain.provider;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductData(
    UUID productId,
    String productName,
    String country,
    String state,
    String city,
    BigDecimal price
) {

}
