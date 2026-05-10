package com.yeoljeong.tripmate.infrastructer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaPayloadDeserializer {
  private final ObjectMapper objectMapper;

  public <T> T deserialize(String payload, Class<T> clazz) throws JsonProcessingException {
    try {
      return objectMapper.readValue(payload, clazz);
    } catch (Exception e) {
      log.error("[KAFKA] 역직렬화 실패 - class {},", clazz, e);
      throw e;
    }
  }
}
