package com.yeoljeong.tripmate.application.service.listener;

import com.yeoljeong.tripmate.domain.events.PlanEvents;
import com.yeoljeong.tripmate.event.PlanUnitParticipantAddedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PlanEventListener {

  private final PlanEvents events;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(PlanUnitParticipantAddedEvent event) {
    events.planUnitAddParticipant(event.eventId(), event.productId(), event.scheduleId(), event.quantity());
  }

}
