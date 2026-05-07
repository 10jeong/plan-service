package com.yeoljeong.tripmate.infrastructer.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoljeong.tripmate.application.port.PlanEvents;
import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import com.yeoljeong.tripmate.event.enums.PlanTopic;
import com.yeoljeong.tripmate.infrastructer.persistence.PlanOutbox;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanOutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
