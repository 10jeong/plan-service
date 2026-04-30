package com.yeoljeong.tripmate.presentation;

import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.application.service.command.PlanInternalCommandService;
import com.yeoljeong.tripmate.application.dto.command.FindParticipantStatusCommand;
import com.yeoljeong.tripmate.presentation.dto.response.FindParticipationStatusResponse;
import com.yeoljeong.tripmate.response.ApiResponse;
import com.yeoljeong.tripmate.response.constants.CommonSuccessCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class PlanInternalController {

  private final PlanInternalCommandService planInternalCommandService;

  @GetMapping("/plan-units/{planUnitId}/participations/users/{userId}")
  public ApiResponse<FindParticipationStatusResponse> findParticipationStatus(
      @PathVariable("planUnitId") UUID planUnitId,
      @PathVariable("userId") UUID userId
  ) {
    FindParticipationStatusResult result = planInternalCommandService.findParticipationStatusByPlanUnitIdAndUserId(
        new FindParticipantStatusCommand(planUnitId, userId));

    FindParticipationStatusResponse response = FindParticipationStatusResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.OK, response);
  }

}
