package com.yeoljeong.tripmate.infrastructer.external.provider;

import com.yeoljeong.tripmate.domain.provider.ProductData;
import com.yeoljeong.tripmate.domain.provider.ProductProvider;
import com.yeoljeong.tripmate.infrastructer.external.client.ProductFeignClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProviderImpl implements ProductProvider {

  private final ProductFeignClient productFeignClient;

  @Override
  public ProductData get(UUID productId) {
    return productFeignClient.get(productId);
  }
}
