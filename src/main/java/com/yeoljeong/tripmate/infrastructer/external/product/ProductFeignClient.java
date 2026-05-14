package com.yeoljeong.tripmate.infrastructer.external.product;

import com.yeoljeong.tripmate.application.dto.external.ProductData;
import com.yeoljeong.tripmate.application.dto.external.ProductSummaryData;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service")
public interface ProductFeignClient {

  @GetMapping("/internal/products/schedules/{scheduleId}")
  ProductData getProduct(@PathVariable("scheduleId") UUID scheduleId);

  @GetMapping("/internal/products/schedules")
  List<ProductSummaryData> getProductList(@RequestParam("scheduleIds") List<UUID> productScheduleIds);
}
