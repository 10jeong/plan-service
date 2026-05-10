package com.yeoljeong.tripmate.application.service.query;

import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.presentation.dto.response.UserHasOpenPlanResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanInternalQueryService {

  private final PlanParticipationRepository planParticipationRepository;

  /* 참여중인 일정 중 OPEN이 존재하는지 확인 (회원 내부 API)*/
  public UserHasOpenPlanResponse hasOpenPlan(UUID userId) {
    boolean existsOpenPlan =  planParticipationRepository.existsOpenPlan(userId);
    return UserHasOpenPlanResponse.from(existsOpenPlan);
  }
}
