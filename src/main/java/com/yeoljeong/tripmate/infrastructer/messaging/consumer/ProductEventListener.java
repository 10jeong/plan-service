package com.yeoljeong.tripmate.infrastructer.messaging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeoljeong.tripmate.application.dto.command.internal.DeductPlanUnitParticipantByProductCommand;
import com.yeoljeong.tripmate.application.service.command.PlanFailureEventService;
import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.event.ProductStockDeductFailedEvent;
import com.yeoljeong.tripmate.event.enums.ProductTopic;
import com.yeoljeong.tripmate.exception.BusinessException;
import com.yeoljeong.tripmate.infrastructer.messaging.KafkaPayloadDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {

  private final PlanInternalCommandService planInternalCommandService;
  private final KafkaPayloadDeserializer kafkaPayloadDeserializer;
  private final PlanFailureEventService planFailureEventService;

  @KafkaListener(topics = ProductTopic.STOCK_DEDUCT_FAILED_TOPIC, groupId = "plan-group")
  public void productStockDeductFailed(String message) throws JsonProcessingException {

    ProductStockDeductFailedEvent event = null;

    try {

      event = kafkaPayloadDeserializer.deserialize(message,
          ProductStockDeductFailedEvent.class);

      planInternalCommandService.deductPlanUnitParticipantByProduct(
          DeductPlanUnitParticipantByProductCommand.from(event));
      log.info("[plan-service] 메시지 업데이트 처리 완료: eventId={}, topic={}", event.eventId(),
          ProductTopic.STOCK_DEDUCT_FAILED_TOPIC);

    } catch (BusinessException e) {
      log.warn("[plan-service] 참여 인원 감소 비즈니스 실패: eventId={}, orderId={}, error={}",
          event.eventId(), event.orderId(), e.getMessage());
      planFailureEventService.deductPlanUnitParticipantFailedByProduct(event, e); // 주문 취소 이벤트 발행
    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패: topic={}, error={}",
          ProductTopic.STOCK_DEDUCT_FAILED_TOPIC, e.getMessage(), e);
      throw e;
    }
  }

}
