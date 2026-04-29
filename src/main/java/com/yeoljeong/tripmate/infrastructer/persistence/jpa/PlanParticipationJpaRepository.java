package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanParticipationJpaRepository extends JpaRepository<PlanParticipation, UUID> {

  boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId);
}
