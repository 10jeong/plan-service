package com.yeoljeong.tripmate.domain.provider;

import com.yeoljeong.tripmate.domain.enums.Country;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductData(
    UUID productId,
    String productName,
    Country country,
    String state,
    String city,
    BigDecimal price
) {

}
