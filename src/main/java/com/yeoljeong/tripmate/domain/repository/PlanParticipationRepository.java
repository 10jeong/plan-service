package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.util.List;
import java.util.UUID;

public interface PlanParticipationRepository {

  void saveAll(List<PlanParticipation> planParticipation);

  PlanParticipation save(PlanParticipation participation);

  boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId);
}
