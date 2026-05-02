package com.yeoljeong.tripmate.infrastructer.external.events.publisher;

import com.yeoljeong.tripmate.domain.events.PlanEvents;
import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import com.yeoljeong.tripmate.event.enums.PlanTopic;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanEventsImpl implements PlanEvents {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void planUnitConfirmed(String eventHash, UUID planUnitId, String planTitle,
      List<UUID> receivers) {

    PlanUnitConfirmedEvent payload = new PlanUnitConfirmedEvent(
        eventHash,
        planUnitId,
        planTitle,
        receivers
    );

    kafkaTemplate.send(PlanTopic.PLAN_UNIT_CONFIRMED_TOPIC, payload);
  }

  @Override
  public void planUnitAddParticipant(UUID eventId, UUID productId, UUID scheduleId, int quantity) {
    PlanUnitParticipantAddedEvent payload = new PlanUnitParticipantAddedEvent(
        eventId,
        productId,
        scheduleId,
        quantity
    );

    kafkaTemplate.send(PlanTopic.PLAN_UNIT_PARTICIPANT_ADDED_TOPIC, payload);
  }
}
