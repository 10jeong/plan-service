package com.yeoljeong.tripmate.infrastructer.messaging.producer;

import com.yeoljeong.tripmate.application.port.PlanEvents;
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
public class PlanEventsAdaptor implements PlanEvents {

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
  public void planUnitAddParticipant(PlanUnitParticipantAddedEvent event) {
        PlanUnitParticipantAddedEvent payload = new PlanUnitParticipantAddedEvent(
        event.eventId(),
        event.productId(),
        event.scheduleId(),
            event.quantity()
    );

    kafkaTemplate.send(PlanTopic.PLAN_UNIT_PARTICIPANT_ADDED_TOPIC, payload);
  }
}
