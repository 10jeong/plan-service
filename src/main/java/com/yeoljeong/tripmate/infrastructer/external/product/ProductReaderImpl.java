package com.yeoljeong.tripmate.infrastructer.external.product;

import com.yeoljeong.tripmate.application.dto.external.ProductData;
import com.yeoljeong.tripmate.application.port.ProductReader;
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
}
