package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanUnitJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanUnitRepositoryImpl implements PlanUnitRepository {

  private final PlanUnitJpaRepository jpaRepository;

  @Override
  public void saveAll(List<PlanUnit> planUnit) {
    jpaRepository.saveAll(planUnit);
  }

  @Override
  public Optional<PlanUnit> findByIdAndPlanId(UUID planUnitId, UUID planId) {
    return jpaRepository.findByIdAndPlanId(planUnitId, planId);
  }

  @Override
  public Optional<PlanUnit> findById(UUID planUnitId) {
    return jpaRepository.findById(planUnitId);
  }

  @Override
  public List<PlanUnit> findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(Plan plan) {
    return jpaRepository.findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(plan);
  }

  @Override
  public int addParticipantCount(UUID planUnitId, int quantity) {
    return jpaRepository.addParticipantCount(planUnitId, quantity);
  }
}
