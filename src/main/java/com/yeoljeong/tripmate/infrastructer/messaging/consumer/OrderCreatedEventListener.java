package com.yeoljeong.tripmate.infrastructer.messaging.consumer;

import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
import com.yeoljeong.tripmate.event.PaymentCompletedEvent;
import com.yeoljeong.tripmate.event.enums.OrderTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreatedEventListener {

  private final PlanInternalCommandService planInternalCommandService;

  @KafkaListener (topics = OrderTopic.ORDER_CREATED_TOPIC, groupId = "plan-group")
  public void orderCreated(OrderCreatedEvent event) { // todo: ack 고려
    try {

      // todo: 이미 처리된 이벤트인지 확인(멱등성 추후 처리)

      if (event == null) {
        log.warn("[plan-service] 주문 생성 이벤트가 null입니다.");
        return;
      }

      planInternalCommandService.addPlanUnitParticipant(event);
      log.info("[plan-service] 메시지 업데이트 처리 완료 : eventId={} ", event.eventId());

    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패 : {} ", e.getMessage(), e);
      throw e;
    }
  }

  public void paymentCompleted(PaymentCompletedEvent event) {
    try {
      if (event == null) {
        log.warn("[plan-service] 결제 완료 이벤트가 null입니다.");
        return;
      }
      planInternalCommandService.updateParticipantStatus(event);
      log.info("[plan-service] 메시지 업데이트 처리 완료 : eventId={} ", event.eventHash());

    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패 : {} ", e.getMessage(), e);
      throw e;
    }
  }
}
