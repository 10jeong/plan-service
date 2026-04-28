package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import java.util.List;

public interface PlanParticipationRepository {

  void saveAll(List<PlanParticipation> planParticipation);
}
