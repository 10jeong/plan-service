package com.yeoljeong.tripmate.infrastructer.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaDltConfig {

  @Value("${spring.retry.interval}")
  private long interval;

  @Value("${spring.retry.max-attempts}")
  private long maxAttempts;

  @Bean
  public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer =
        new DeadLetterPublishingRecoverer(
            kafkaTemplate,
            (record, exception) -> {
              String dltTopic = record.topic() + ".DLT";

              log.error("[KAFKA-DLT] 메시지 DLT 전송 : originTopic={}, dltTopic={}, key={}, error={}",
                  record.topic(),
                  dltTopic,
                  record.key(),
                  exception.getMessage(),
                  exception);

              return new TopicPartition(dltTopic, (record.partition()));
            }
        );
    FixedBackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);

    return new DefaultErrorHandler(recoverer, fixedBackOff);
  }
}
