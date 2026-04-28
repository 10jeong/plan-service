package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanParticipationJpaRepository;
import java.util.List;
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
}
