package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanUnitJpaRepository;
import java.util.List;
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
}
