package com.yeoljeong.tripmate.infrastructer.persistence;

import com.yeoljeong.tripmate.domain.Outbox;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "plan_outbox")
@Getter
public class PlanOutbox extends Outbox {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  public static PlanOutbox create(String topic, String payload) {
    PlanOutbox outBox = new PlanOutbox();
    init(outBox, topic, payload);
    return outBox;
  }
}
