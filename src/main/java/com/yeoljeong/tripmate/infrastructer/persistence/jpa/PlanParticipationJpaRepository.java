package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanParticipationJpaRepository extends JpaRepository<PlanParticipation, UUID> {

  boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId);

  Optional<PlanParticipation> findByPlanUnitAndUserId(PlanUnit planUnit, UUID userId);

  Optional<PlanParticipation> findByIdAndPlanUnit(UUID participationId, PlanUnit planUnit);

  Optional<PlanParticipation> findByPlanUnit_IdAndUserId(UUID planUnitId, UUID userId);

  boolean existsByPlanUnitAndUserIdAndParticipationRole(PlanUnit planUnit, UUID userId, ParticipationRole participationRole);

  List<PlanParticipation> findAllByPlanUnitAndParticipationStatus(PlanUnit planUnit, ParticipationStatus participationStatus);

  List<PlanParticipation> findAllByPlanUnitIn(List<PlanUnit> planUnit);

  @Modifying
  @Query("""
    update PlanParticipation p
      set p.participationStatus = :nextStatus
        where p.id = :participationId
            and p.participationStatus = :currentStatus
  """)
  int updateStatus(
      @Param("participationId") UUID participationId,
      @Param("currentStatus") ParticipationStatus currentStatus,
      @Param("nextStatus") ParticipationStatus nextStatus);
}
