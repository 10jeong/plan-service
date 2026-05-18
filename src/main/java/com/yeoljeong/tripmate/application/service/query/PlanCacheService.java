package com.yeoljeong.tripmate.application.service.query;

import com.yeoljeong.tripmate.application.dto.result.GetPlanDetailResult;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanCacheService {

  private final PlanQueryService planQueryService;

  @Cacheable(cacheNames = "planDetail", key = "#planId")
  public GetPlanDetailResult getPlanDetail(UUID planId) {
    return planQueryService.getPlanDetailFromDb(planId);
  }

}
