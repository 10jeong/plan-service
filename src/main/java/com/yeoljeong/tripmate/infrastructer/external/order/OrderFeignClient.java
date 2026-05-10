package com.yeoljeong.tripmate.infrastructer.external.order;

import com.yeoljeong.tripmate.application.dto.external.OrderPlanUnitData;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderFeignClient {

  @GetMapping("internal/orders/{orderId}")
  OrderPlanUnitData getPlanUnitId(@PathVariable("orderId") UUID orderId);
}
