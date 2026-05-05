package com.yeoljeong.tripmate.infrastructer.external.client;

import com.yeoljeong.tripmate.domain.provider.ProductData;
import com.yeoljeong.tripmate.domain.provider.ProductProvider;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProviderImpl implements ProductProvider {

  private final ProductFeignClient productFeignClient;

  @Override
  public ProductData getProduct(UUID scheduleId) {
    return productFeignClient.getProduct(scheduleId);
  }
}
