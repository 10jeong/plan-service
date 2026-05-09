package com.yeoljeong.tripmate.infrastructer.messaging.consumer;

import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.event.PaymentCompletedEvent;
import com.yeoljeong.tripmate.event.enums.PaymentTopic;
import com.yeoljeong.tripmate.infrastructer.messaging.KafkaPayloadDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventListener {
  private final PlanInternalCommandService planInternalCommandService;
  private final KafkaPayloadDeserializer kafkaPayloadDeserializer;

  @KafkaListener(topics = PaymentTopic.PAYMENT_COMPLETED_TOPIC, groupId = "plan-group")
  public void paymentCompleted(String message) {
    try {

      PaymentCompletedEvent event = kafkaPayloadDeserializer.deserialize(message,
          PaymentCompletedEvent.class);

      planInternalCommandService.updateParticipantStatus(event);
      log.info("[plan-service] 메시지 업데이트 처리 완료 : eventId={}, topic={}", event.eventHash(),
          PaymentTopic.PAYMENT_COMPLETED_TOPIC);

    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패 : topic={}, error={} ",
          PaymentTopic.PAYMENT_COMPLETED_TOPIC,
          e.getMessage(), e);
      throw e;
    }
  }
}
