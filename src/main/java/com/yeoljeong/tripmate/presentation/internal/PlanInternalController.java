package com.yeoljeong.tripmate.presentation.internal;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.application.service.query.PlanInternalQueryService;
import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.presentation.dto.response.FindParticipationStatusResponse;
import com.yeoljeong.tripmate.presentation.dto.response.UserHasOpenPlanResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PlanInternalController {

  private final PlanInternalCommandService planInternalCommandService;
  private final PlanInternalQueryService planInternalQueryService;

  @GetMapping("/plan-units/{planUnitId}/participations/users/{userId}")
  public FindParticipationStatusResponse findParticipationStatus(
      @PathVariable("planUnitId") UUID planUnitId,
      @PathVariable("userId") UUID userId
  ) {
    FindParticipationStatusResult result = planInternalCommandService.findParticipationStatusByPlanUnitIdAndUserId(planUnitId, userId);

    FindParticipationStatusResponse response = FindParticipationStatusResponse.from(result);

    return response;
  }

  /* 참여중인 일정 중 OPEN이 존재하는지 확인 (회원 내부 API)*/
  @GetMapping("/plan/withdrawal-check")
  public UserHasOpenPlanResponse hasOpenPlan(
      @RequestParam("userId") UUID userId
  ) {
    return planInternalQueryService.hasOpenPlan(userId);
  }

}
