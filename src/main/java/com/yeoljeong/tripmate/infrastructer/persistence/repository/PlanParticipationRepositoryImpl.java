package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
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
  public boolean existsByPlanUnitIdAndUserId(UUID planUnitId, UUID guestId) {
    return jpaRepository.existsByPlanUnitIdAndUserId(planUnitId, guestId);
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
}
