package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanParticipationRepository {

  void saveAll(List<PlanParticipation> planParticipation);

  PlanParticipation save(PlanParticipation participation);

  boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId);

  Optional<PlanParticipation> findByPlanUnitAndUserId(PlanUnit planUnit, UUID uuid);

  Optional<PlanParticipation> findByIdAndPlanUnit(UUID uuid, PlanUnit planUnit);

  Optional<PlanParticipation> findByPlanUnit_IdAndUserId(UUID planUnitId, UUID userId);
}
