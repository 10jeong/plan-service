package com.yeoljeong.tripmate.infrastructer.external.client;

import com.yeoljeong.tripmate.domain.provider.ProductData;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface ProductFeignClient {

  @GetMapping("/internal/products/{productId}")
  ProductData getProduct(@PathVariable("productId") UUID productId);
}
