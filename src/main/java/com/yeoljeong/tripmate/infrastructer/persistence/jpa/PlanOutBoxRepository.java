package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.constants.OutboxStatus;
import com.yeoljeong.tripmate.infrastructer.persistence.PlanOutbox;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanOutBoxRepository extends JpaRepository<PlanOutbox, UUID> {

  List<PlanOutbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);
}
