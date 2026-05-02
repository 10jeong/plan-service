package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.plan.Plan;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlanRepository {

  Plan save(Plan plan);

  boolean existsById(UUID planId);

  Optional<Plan> findById(UUID planId);

  Slice<Plan> findByCondition(String title, LocalDate startDate, LocalDate endDate, RecruitStatus recruitStatus,
      Pageable pageable);
}
