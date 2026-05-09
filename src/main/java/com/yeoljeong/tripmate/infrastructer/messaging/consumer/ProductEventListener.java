package com.yeoljeong.tripmate.infrastructer.messaging.consumer;

import com.yeoljeong.tripmate.application.dto.command.internal.DeductPlanUnitParticipantCommand;
import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.event.ProductStockDeductFailedEvent;
import com.yeoljeong.tripmate.event.enums.ProductTopic;
import com.yeoljeong.tripmate.infrastructer.messaging.KafkaPayloadDeserializer;
import java.security.NoSuchAlgorithmException;
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

  @KafkaListener(topics = ProductTopic.STOCK_DEDUCT_FAILED_TOPIC, groupId = "plan-group")
  public void productStockDeductFailed(String message) throws NoSuchAlgorithmException {

    try {
      ProductStockDeductFailedEvent event = kafkaPayloadDeserializer.deserialize(message,
          ProductStockDeductFailedEvent.class);

      if (event == null) {
        log.warn("[plan-service] 상품 재고 차감 실패 이벤트가 null입니다.");
      }
      planInternalCommandService.deductPlanUnitParticipant(DeductPlanUnitParticipantCommand.from(event));
      log.info("[plan-service] 메시지 업데이트 처리 완료: eventId={}", event.eventId());

    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패: {}", e.getMessage(), e);
      throw e;
    }



  }

}
