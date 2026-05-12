package com.yeoljeong.tripmate.infrastructer.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoljeong.tripmate.application.port.PlanEvents;
import com.yeoljeong.tripmate.event.PlanUnitAddParticipantFailedEvent;
import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitDeductParticipantByProductEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantQuitEvent;
import com.yeoljeong.tripmate.event.enums.PlanTopic;
import com.yeoljeong.tripmate.infrastructer.persistence.PlanOutbox;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanOutBoxRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanEventsAdaptor implements PlanEvents {

  private final PlanOutBoxRepository outBoxRepository;
  private final ObjectMapper objectMapper;


  @Override
  public void planUnitConfirmed(PlanUnitConfirmedEvent event) {
    try {
      String json = objectMapper.writeValueAsString(event);
      outBoxRepository.save(PlanOutbox.create(PlanTopic.PLAN_UNIT_CONFIRMED_TOPIC, json));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }
  }

  @Override
  public void planUnitAddParticipant(PlanUnitParticipantAddedEvent event) {

    try {
      String json = objectMapper.writeValueAsString(event);
      outBoxRepository.save(
          PlanOutbox.create(PlanTopic.PLAN_UNIT_PARTICIPANT_ADDED_TOPIC,
              json)
      );

    } catch (JsonProcessingException e) {
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }
  }

  /* 참여 현재 인원 감소 이벤트*/
  @Override
  public void deductPlanUnitParticipantByProduct(UUID eventId, UUID orderId) {

    try {
      PlanUnitDeductParticipantByProductEvent payload = new PlanUnitDeductParticipantByProductEvent(
          eventId, orderId);

      String json = objectMapper.writeValueAsString(payload);
      outBoxRepository.save(
          PlanOutbox.create(PlanTopic.PLAN_UNIT_PARTICIPANT_DEDUCTED_BY_PRODUCT_TOPIC,
              json)
      );
    } catch (JsonProcessingException e) {
      log.error(
          "[plan-service] 참여 인원 감소 이벤트 직렬화 실패: eventId={}, topic={}",
          eventId,
          PlanTopic.PLAN_UNIT_PARTICIPANT_DEDUCTED_BY_PRODUCT_TOPIC,
          e
      );
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }
  }

  /* 참여 현재 인원 증가 실패 이벤트*/
  @Override
  public void addPlanUnitParticipantFailed(UUID eventId, UUID orderId) {
    try {
      PlanUnitAddParticipantFailedEvent payload = new PlanUnitAddParticipantFailedEvent(eventId,
          orderId);
      String json = objectMapper.writeValueAsString(payload);

      outBoxRepository.save(
          PlanOutbox.create(PlanTopic.PLAN_UNIT_PARTICIPANT_ADD_FAILED_TOPIC, json)
      );
    } catch (JsonProcessingException e) {
      log.error(
          "[plan-service] 참여 인원 증가 실패 이벤트 직렬화 실패: eventId={}, topic={}",
          eventId,
          PlanTopic.PLAN_UNIT_PARTICIPANT_ADD_FAILED_TOPIC,
          e
      );
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }
  }

  /* 참여 현재 인원 감소 실패 이벤트*/
  @Override
  public void deductPlanUnitParticipantFailedByProduct(UUID eventId, UUID orderId) {
    try {
      PlanUnitDeductParticipantByProductEvent payload = new PlanUnitDeductParticipantByProductEvent(eventId,
          orderId);
      String json = objectMapper.writeValueAsString(payload);

      outBoxRepository.save(
          PlanOutbox.create(PlanTopic.PLAN_UNIT_PARTICIPANT_DEDUCT_FAILED_TOPIC, json)
      );
    } catch (JsonProcessingException e) {
      log.error(
          "[plan-service] 참여 인원 감소 실패 이벤트 직렬화 실패: eventId={}, topic={}",
          eventId,
          PlanTopic.PLAN_UNIT_PARTICIPANT_DEDUCT_FAILED_TOPIC,
          e
      );
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }

  }

  /* 참여 합류된 사용자 탈퇴 이벤트*/
  @Override
  public void planUnitParticipationQuit(UUID eventId, UUID userId, UUID planUnitId, String reason) {
    try {
      PlanUnitParticipantQuitEvent payload = new PlanUnitParticipantQuitEvent(eventId,
          userId, planUnitId, reason);
      String json = objectMapper.writeValueAsString(payload);

      outBoxRepository.save(
          PlanOutbox.create(PlanTopic.PLAN_UNIT_PARTICIPANT_QUIT_TOPIC, json)
      );
    } catch (JsonProcessingException e) {
      log.error(
          "[plan-service] 참여 합류된 사용자 탈퇴 이벤트 직렬화 실패: eventId={}, topic={}",
          eventId,
          PlanTopic.PLAN_UNIT_PARTICIPANT_QUIT_TOPIC,
          e
      );
      throw new RuntimeException("이벤트 직렬화 실패", e);
    }
  }
}
