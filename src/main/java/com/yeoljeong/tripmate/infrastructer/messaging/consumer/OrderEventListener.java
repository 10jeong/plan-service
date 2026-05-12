package com.yeoljeong.tripmate.infrastructer.messaging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeoljeong.tripmate.application.dto.command.internal.DeductPlanUnitParticipantByOrderCommand;
import com.yeoljeong.tripmate.application.service.command.PlanFailureEventService;
import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.event.OrderCreatedEvent;
import com.yeoljeong.tripmate.event.OrderSchedulerCancelledEvent;
import com.yeoljeong.tripmate.event.enums.OrderTopic;
import com.yeoljeong.tripmate.exception.BusinessException;
import com.yeoljeong.tripmate.infrastructer.messaging.KafkaPayloadDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderEventListener {

  private final PlanInternalCommandService planInternalCommandService;
  private final KafkaPayloadDeserializer kafkaPayloadDeserializer;
  private final PlanFailureEventService planFailureEventService;

  @KafkaListener (topics = OrderTopic.ORDER_CREATED_TOPIC, groupId = "plan-group")
  public void orderCreated(String message) throws JsonProcessingException {

    OrderCreatedEvent event = null;

    try {

      // todo: 이미 처리된 이벤트인지 확인(멱등성 추후 처리)

      event = kafkaPayloadDeserializer.deserialize(message,
          OrderCreatedEvent.class);

      planInternalCommandService.addPlanUnitParticipant(event);
      log.info("[plan-service] 메시지 업데이트 처리 완료 : eventId={}, topic={}", event.eventId(),
          OrderTopic.ORDER_CREATED_TOPIC);

    } catch (BusinessException e) {
      log.warn("[plan-service] 주문 생성 이벤트 비즈니스 처리 실패. 실패 이벤트 발행 후 소비 완료 처리 : "
              + "topic={}, eventId={}, orderId={}, planUnitId={}, error={}",
          OrderTopic.ORDER_CREATED_TOPIC, event.eventId(), event.orderId(), event.planUnitId(),
          e.getMessage(), e);
      planFailureEventService.addPlanUnitParticipantFailed(event, e);

    } catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패 : topic={}, error={}",
          OrderTopic.ORDER_CREATED_TOPIC, e.getMessage(), e);
      throw e;
    }
  }

  @KafkaListener(topics = OrderTopic.ORDER_SCHEDULER_CANCELLED_TOPIC, groupId = "plan-group")
  public void orderSchedulerCancelled(String message) throws JsonProcessingException {
    OrderSchedulerCancelledEvent event = null;

    try {
      event = kafkaPayloadDeserializer.deserialize(message, OrderSchedulerCancelledEvent.class);

      planInternalCommandService.deductPlanUnitParticipantByOrder(
          DeductPlanUnitParticipantByOrderCommand.from(event));
      log.info("[plan-service] 메시지 업데이트 처리 완료 : eventId={}, topic={}", event.eventId(),
          OrderTopic.ORDER_SCHEDULER_CANCELLED_TOPIC);

    } catch (BusinessException e) {
      log.warn("[plan-service] 스케줄러로 인한 주문 취소 이벤트 비즈니스 처리 실패. 실패 이벤트 발행 후 소비 완료 처리 : "
              + "topic={}, eventId={}, planUnitId={}, scheduleId={} error={}",
          OrderTopic.ORDER_SCHEDULER_CANCELLED_TOPIC, event.eventId(), event.planUnitId(), event.scheduleId(),
          e.getMessage(), e);
      // todo: dlt 저장
//      planFailureEventService.addPlanUnitParticipantFailed(event, e);
      throw e; // DLT/실패이벤트 발행 구현 전까지는 유실 방지를 위해 재시도 경로 유지

    }catch (Exception e) {
      log.error("[plan-service] 메시지 업데이트 처리 실패 : topic={}, error={}",
          OrderTopic.ORDER_SCHEDULER_CANCELLED_TOPIC, e.getMessage(), e);
      throw e;
    }

  }


}
