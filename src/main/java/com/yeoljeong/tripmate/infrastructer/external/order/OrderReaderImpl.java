package com.yeoljeong.tripmate.infrastructer.external.order;

import com.yeoljeong.tripmate.application.dto.external.OrderPlanUnitData;
import com.yeoljeong.tripmate.application.port.OrderReader;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReaderImpl implements OrderReader {

  private final OrderFeignClient orderFeignClient;

  @Override
  public OrderPlanUnitData getPlanUnitId(UUID orderId) {
    return orderFeignClient.getPlanUnitId(orderId);
  }
}
