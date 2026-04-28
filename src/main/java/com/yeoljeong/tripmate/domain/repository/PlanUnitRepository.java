package com.yeoljeong.tripmate.domain.repository;

import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import java.util.List;

public interface PlanUnitRepository {

  void saveAll(List<PlanUnit> planUnit);
}
