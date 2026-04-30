package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanParticipationJpaRepository extends JpaRepository<PlanParticipation, UUID> {

  boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId);

  Optional<PlanParticipation> findByPlanUnitAndUserId(PlanUnit planUnit, UUID userId);

  Optional<PlanParticipation> findByIdAndPlanUnit(UUID participationId, PlanUnit planUnit);

  PlanParticipation findByPlanUnit_IdAndUserId(UUID planUnitId, UUID userId);
}
