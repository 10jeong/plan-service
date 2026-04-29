package com.yeoljeong.tripmate.presentation;

import com.yeoljeong.tripmate.application.dto.command.ParticipatePlanCommand;
import com.yeoljeong.tripmate.application.dto.result.ParticipatePlanResult;
import com.yeoljeong.tripmate.application.service.command.PlanCommandService;
import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import com.yeoljeong.tripmate.presentation.dto.request.CreatePlanRequest;
import com.yeoljeong.tripmate.presentation.dto.response.CreatePlanResponse;
import com.yeoljeong.tripmate.presentation.dto.response.ParticipatePlanResponse;
import com.yeoljeong.tripmate.response.ApiResponse;
import com.yeoljeong.tripmate.response.constants.CommonSuccessCode;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

  private final PlanCommandService planCommandService;

  @PostMapping
  public ApiResponse<CreatePlanResponse> createPlans(
      @RequestHeader("X-USER-ID") UUID userId,
      @RequestBody @Valid CreatePlanRequest createPlanRequest) {

    CreatePlanResult result = planCommandService.createPlans(createPlanRequest.toCommand(userId));
    CreatePlanResponse response = CreatePlanResponse.from(result);
    return ApiResponse.success(CommonSuccessCode.CREATE, "일정 등록이 되었습니다.", response);
  }


  @PostMapping("/plans/{planId}/unit-plans/{unitPlanId}/participations")
  public ApiResponse<ParticipatePlanResponse> participatePlan(
      @RequestHeader("X-USER-ID") UUID userId,
      @PathVariable("planId") UUID planId,
      @PathVariable("unitPlanId") UUID planUnitId) {

    ParticipatePlanResult result =
        planCommandService.participatePlanUnit(new ParticipatePlanCommand(userId, planId, planUnitId));
    ParticipatePlanResponse response = ParticipatePlanResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.CREATE, "일정 참여 신청이 완료되었습니다.",
        response);
  }


}
