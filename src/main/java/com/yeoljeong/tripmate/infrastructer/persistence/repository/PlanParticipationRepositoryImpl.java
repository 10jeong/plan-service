package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanParticipationJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanParticipationRepositoryImpl implements PlanParticipationRepository {

  private final PlanParticipationJpaRepository jpaRepository;

  @Override
  public void saveAll(List<PlanParticipation> planParticipation) {
    jpaRepository.saveAll(planParticipation);
  }

  @Override
  public PlanParticipation save(PlanParticipation participation) {
    return jpaRepository.save(participation);
  }

  @Override
  public boolean existsByPlanUnitIdAndUserIdAndIsDeletedIsFalse(UUID planUnitId, UUID guestId) {
    return jpaRepository.existsByPlanUnitIdAndUserIdAndIsDeletedIsFalse(planUnitId, guestId);
  }

  @Override
  public Optional<PlanParticipation> findByPlanUnitAndUserId(PlanUnit planUnit, UUID userId) {
    return jpaRepository.findByPlanUnitAndUserId(planUnit, userId);
  }

  @Override
  public Optional<PlanParticipation> findByIdAndPlanUnit(UUID participationId, PlanUnit planUnit) {
    return jpaRepository.findByIdAndPlanUnit(participationId, planUnit);
  }

  @Override
  public Optional<PlanParticipation> findByPlanUnit_IdAndUserId(UUID planUnitId, UUID userId) {
    return jpaRepository.findByPlanUnit_IdAndUserId(planUnitId, userId);
  }

  @Override
  public boolean existsByPlanUnitAndUserIdAndParticipationRole(PlanUnit planUnit, UUID userId,
      ParticipationRole participationRole) {
    return jpaRepository.existsByPlanUnitAndUserIdAndParticipationRole(planUnit, userId,
        participationRole);
  }

  @Override
  public List<PlanParticipation> findAllByPlanUnitAndParticipationStatus(PlanUnit planUnit,
      ParticipationStatus participationStatus) {
    return jpaRepository.findAllByPlanUnitAndParticipationStatus(planUnit, participationStatus);
  }

  @Override
  public List<PlanParticipation> findAllByPlanUnitIn(List<PlanUnit> planUnit) {
    return jpaRepository.findAllByPlanUnitIn(planUnit);
  }

  @Override
  public int updateStatus(UUID planUnitId, ParticipationStatus currentStatus,
      ParticipationStatus nextStatus) {
    return jpaRepository.updateStatus(planUnitId, currentStatus, nextStatus);
  }

  @Override
  public boolean existsOpenPlan(UUID userId) {
    return jpaRepository.existsOpenPlan(userId);
  }


  @Override
  public List<PlanParticipation> findGuestRequestsByPlans(List<Plan> planSlice) {
    return jpaRepository.findGuestRequestsByPlans(planSlice);
  }

  @Override
  public List<PlanParticipation> findAllByUserIdAndPlanUnit_PlanInAndParticipationRoleAndIsDeletedFalse(
      UUID userId, List<Plan> content, ParticipationRole participationRole) {
    return jpaRepository.findAllByUserIdAndPlanUnit_PlanInAndParticipationRoleAndIsDeletedFalse(
        userId, content, participationRole);
  }
}
