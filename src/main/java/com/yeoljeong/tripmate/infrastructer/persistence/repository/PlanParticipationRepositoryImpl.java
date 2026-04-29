package com.yeoljeong.tripmate.infrastructer.persistence.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.infrastructer.persistence.jpa.PlanParticipationJpaRepository;
import java.util.List;
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
}
