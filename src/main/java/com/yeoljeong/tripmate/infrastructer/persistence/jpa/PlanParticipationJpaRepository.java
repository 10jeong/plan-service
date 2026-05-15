package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanParticipationJpaRepository extends JpaRepository<PlanParticipation, UUID> {

  boolean existsByPlanUnitIdAndUserIdAndIsDeletedIsFalse(UUID planUnitId, UUID guestId);

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

  @Query("""
    select count(pa) > 0
      from PlanParticipation pa
        join pa.planUnit u
        join u.plan p
          where pa.userId = :userId
            and pa.participationStatus in (
              com.yeoljeong.tripmate.domain.enums.ParticipationStatus.RESERVED,
              com.yeoljeong.tripmate.domain.enums.ParticipationStatus.CONFIRMED
              )
            and p.recruitStatus = com.yeoljeong.tripmate.domain.enums.RecruitStatus.OPEN
  """)
  boolean existsOpenPlan(@Param("userId") UUID userId);

  @Query("""
    select distinct p
      from PlanParticipation pp
        join pp.planUnit pu
        join pu.plan p
          where pp.userId = :userId
            and pp.isDeleted = false
  """)
  Slice<Plan> findMyParticipatedPlans(@Param("userId") UUID userId, Pageable pageable);

  List<PlanParticipation> findAllByUserIdAndPlanUnit_PlanIn(UUID userId, List<Plan> plans);
}
