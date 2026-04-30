package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanProductSnapshot;
import com.yeoljeong.tripmate.domain.repository.PlanProductSnapshotRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanProductSnapshotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanProductSnapshotRepositoryImpl implements PlanProductSnapshotRepository {

  private final PlanProductSnapshotJpaRepository jpaRepository;
  @Override
  public PlanProductSnapshot save(PlanProductSnapshot planProductSnapshot) {
    return jpaRepository.save(planProductSnapshot);
  }
}
