package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.Plan;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository {

  Plan save(Plan plan);

  boolean existsById(UUID planId);

  Optional<Plan> findById(UUID planId);
}
