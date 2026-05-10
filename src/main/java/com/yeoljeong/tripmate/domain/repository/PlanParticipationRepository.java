package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
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

  boolean existsByPlanUnitAndUserIdAndParticipationRole(PlanUnit planUnit, UUID userId, ParticipationRole participationRole);

  List<PlanParticipation> findAllByPlanUnitAndParticipationStatus(PlanUnit planUnit, ParticipationStatus participationStatus);

  List<PlanParticipation> findAllByPlanUnitIn(List<PlanUnit> planUnit);

  int updateStatus(UUID participationId, ParticipationStatus currentStatus, ParticipationStatus nextStatus);

  Optional<PlanParticipation> findById(UUID participationId);
}
