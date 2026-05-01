package com.yeoljeong.tripmate.infrastructer.external.client;

import com.yeoljeong.tripmate.domain.provider.ProductData;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

  @GetMapping("/internal/products/{productId}")
  ProductData get(UUID productId);
}
