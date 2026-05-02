package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.plan.Plan;
import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanUnitJpaRepository extends JpaRepository<PlanUnit, UUID> {

  Optional<PlanUnit> findByIdAndPlanId(UUID planUnitId, UUID planId);

  List<PlanUnit> findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(Plan plan);
}
