package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.application.dto.external.ProductSummaryData;
import java.math.BigDecimal;
import java.util.UUID;

public record PlanProductSummary(
    UUID productId,
    UUID scheduleId,
    String productName,
    BigDecimal price
) {

  public static PlanProductSummary from(ProductSummaryData productSummaryData) {
    return new PlanProductSummary(
        productSummaryData.productId(),
        productSummaryData.productScheduleId(),
        productSummaryData.productName(),
        productSummaryData.price()
    );
  }
}
