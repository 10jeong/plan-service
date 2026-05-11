package com.yeoljeong.tripmate.application.port;


import com.yeoljeong.tripmate.application.dto.external.OrderPlanUnitData;
import java.util.UUID;

public interface OrderReader {

  OrderPlanUnitData getPlanUnitId(UUID orderId);
}
