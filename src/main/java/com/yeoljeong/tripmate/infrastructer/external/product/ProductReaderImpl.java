package com.yeoljeong.tripmate.infrastructer.external.product;

import com.yeoljeong.tripmate.application.dto.external.ProductData;
import com.yeoljeong.tripmate.application.dto.external.ProductSummaryData;
import com.yeoljeong.tripmate.application.port.ProductReader;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReaderImpl implements ProductReader {

  private final ProductFeignClient productFeignClient;

  @Override
  public ProductData getProduct(UUID scheduleId) {
    return productFeignClient.getProduct(scheduleId);
  }

  @Override
  public List<ProductSummaryData> getProductList(List<UUID> productScheduleIds) {
    return productFeignClient.getProductList(productScheduleIds);
  }
}
