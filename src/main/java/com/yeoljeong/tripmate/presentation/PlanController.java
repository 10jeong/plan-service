package com.yeoljeong.tripmate.presentation;

import com.yeoljeong.tripmate.application.service.command.PlanCommandService;
import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import com.yeoljeong.tripmate.presentation.dto.request.CreatePlanRequest;
import com.yeoljeong.tripmate.presentation.dto.response.CreatePlanResponse;
import com.yeoljeong.tripmate.response.ApiResponse;
import com.yeoljeong.tripmate.response.constants.CommonSuccessCode;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
      @RequestHeader("X-USER_ID") UUID userId,
      @RequestBody @Valid CreatePlanRequest createPlanRequest) {

    CreatePlanResult result = planCommandService.createPlans(createPlanRequest.toCommand(userId));
    CreatePlanResponse response = CreatePlanResponse.from(result);
    return ApiResponse.success(CommonSuccessCode.CREATE, "일정 등록이 되었습니다.", response);

  }

}
