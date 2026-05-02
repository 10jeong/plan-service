package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.Plan;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanUnitRepository {

  void saveAll(List<PlanUnit> planUnit);

  Optional<PlanUnit> findByIdAndPlanId(UUID planUnitId, UUID planId);

  Optional<PlanUnit> findById(UUID uuid);

  List<PlanUnit> findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(Plan plan);
}
