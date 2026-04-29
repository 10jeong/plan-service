package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.Plan;
import java.util.UUID;

public interface PlanRepository {

  Plan save(Plan plan);

  boolean existsById(UUID planId);
}
