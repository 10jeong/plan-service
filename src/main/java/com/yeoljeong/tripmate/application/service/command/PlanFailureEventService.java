package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.port.PlanEvents;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
import com.yeoljeong.tripmate.event.ProductStockDeductFailedEvent;
import com.yeoljeong.tripmate.exception.BusinessException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanFailureEventService {

  private final PlanEvents events;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void addPlanUnitParticipantFailed(OrderCreatedEvent event, BusinessException e) {
    // todo: e.message 전달
    events.addPlanUnitParticipantFailed(UUID.randomUUID(), event.orderId());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deductPlanUnitParticipantFailedByProduct(ProductStockDeductFailedEvent event, BusinessException e) {
    events.deductPlanUnitParticipantFailedByProduct(UUID.randomUUID(), event.orderId());
  }
}
