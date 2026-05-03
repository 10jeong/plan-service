package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.repository.PlanRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {

  private final PlanJpaRepository jpaRepository;

  @Override
  public Plan save(Plan plan) {
    return jpaRepository.save(plan);
  }

  @Override
  public boolean existsById(UUID planId) {
    return jpaRepository.existsById(planId);
  }

  @Override
  public Optional<Plan> findById(UUID planId) {
    return jpaRepository.findById(planId);
  }
}
