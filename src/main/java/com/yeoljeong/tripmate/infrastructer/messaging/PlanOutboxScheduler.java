package com.yeoljeong.tripmate.infrastructer.messaging;


import com.yeoljeong.tripmate.domain.constants.OutboxStatus;
import com.yeoljeong.tripmate.infrastructer.persistence.PlanOutbox;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanOutBoxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PlanOutboxScheduler {

  private final PlanOutBoxRepository planOutBoxRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Scheduled(fixedDelay = 3000)
  @Transactional
  public void publish() {
    List<PlanOutbox> pending = planOutBoxRepository.findTop100ByStatusOrderByCreatedAtAsc(
        OutboxStatus.PENDING);

    for (PlanOutbox outbox : pending) {
      try {
        kafkaTemplate.send(outbox.getTopic(), outbox.getPayload())
            .get(); // ack 기다림
        outbox.published();
        log.info("Outbox Relay 성공: id={}, topic={}", outbox.getId(), outbox.getTopic());
      } catch (Exception e) {
        outbox.fail();
        log.error("Outbox Relay 실패: id={}, topic={}, retryCount={}", outbox.getId(),
            outbox.getTopic(), outbox.getRetryCount());
      }
    }
  }

}
