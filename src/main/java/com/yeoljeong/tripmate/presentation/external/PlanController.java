package com.yeoljeong.tripmate.presentation.external;

import com.yeoljeong.tripmate.application.dto.command.GetPlanCommand;
import com.yeoljeong.tripmate.application.dto.command.ParticipatePlanCommand;
import com.yeoljeong.tripmate.application.dto.result.ConfirmPlanUnitResult;
import com.yeoljeong.tripmate.application.dto.result.GetPlanDetailResult;
import com.yeoljeong.tripmate.application.dto.result.ParticipatePlanResult;
import com.yeoljeong.tripmate.application.dto.result.UpdateParticipationStatusResult;
import com.yeoljeong.tripmate.application.service.command.PlanCommandService;
import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import com.yeoljeong.tripmate.application.service.query.PlanQueryService;
import com.yeoljeong.tripmate.auth.annotation.LoginUser;
import com.yeoljeong.tripmate.auth.context.UserContext;
import com.yeoljeong.tripmate.presentation.dto.request.CreatePlanRequest;
import com.yeoljeong.tripmate.presentation.dto.request.PlanSearchCondition;
import com.yeoljeong.tripmate.presentation.dto.request.UpdateParticipationStatusRequest;
import com.yeoljeong.tripmate.presentation.dto.response.ConfirmPlanUnitResponse;
import com.yeoljeong.tripmate.presentation.dto.response.CreatePlanResponse;
import com.yeoljeong.tripmate.presentation.dto.response.GetPlanDetailResponse;
import com.yeoljeong.tripmate.presentation.dto.response.ParticipatePlanResponse;
import com.yeoljeong.tripmate.presentation.dto.response.UpdateParticipationStatusResponse;
import com.yeoljeong.tripmate.presentation.dto.response.GetPlanResponse;
import com.yeoljeong.tripmate.response.ApiResponse;
import com.yeoljeong.tripmate.response.constants.CommonSuccessCode;
import jakarta.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

  private final PlanCommandService planCommandService;
  private final PlanQueryService planQueryService;

  @PostMapping
  public ApiResponse<CreatePlanResponse> createPlans(
      @LoginUser UserContext user,
      @RequestBody @Valid CreatePlanRequest createPlanRequest) {

    CreatePlanResult result = planCommandService.createPlans(createPlanRequest.toCommand(user.userId()));
    CreatePlanResponse response = CreatePlanResponse.from(result);
    return ApiResponse.success(CommonSuccessCode.OK, "일정 등록이 되었습니다.", response);
  }


  @PostMapping("/{planId}/unit-plans/{unitPlanId}/participations")
  public ApiResponse<ParticipatePlanResponse> participatePlan(
      @LoginUser UserContext user,
      @PathVariable("planId") UUID planId,
      @PathVariable("unitPlanId") UUID planUnitId) {

    ParticipatePlanResult result =
        planCommandService.participatePlanUnit(new ParticipatePlanCommand(user.userId(), planId, planUnitId));
    ParticipatePlanResponse response = ParticipatePlanResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.OK, "일정 참여 신청이 완료되었습니다.",
        response);
  }

  @PatchMapping("/{planId}/unit-plans/{unitPlanId}/participations/{participationId}/status")
  public ApiResponse<UpdateParticipationStatusResponse> updateParticipationStatus(
      @LoginUser UserContext user,
      @PathVariable("planId") UUID planId,
      @PathVariable("unitPlanId") UUID planUnitId,
      @PathVariable("participationId") UUID participationId,
      @RequestBody @Valid UpdateParticipationStatusRequest request) {

    UpdateParticipationStatusResult result =
        planCommandService.updateParticipationStatus(
            request.toCommand(user.userId(), planId, planUnitId, participationId));

    UpdateParticipationStatusResponse response = UpdateParticipationStatusResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.OK, "일정 참여 상태가 변경되었습니다.", response);

  }

  // 일정 확정
  @PatchMapping("/{planId}/unit-plans/{unitPlanId}")
  public ApiResponse<ConfirmPlanUnitResponse> confirmPlanUnit(
      @PathVariable("planId") UUID planId,
      @PathVariable("unitPlanId") UUID planUnitId,
      @LoginUser UserContext user
      ) throws NoSuchAlgorithmException {
    ConfirmPlanUnitResult result = planCommandService.confirmPlanUnit(planId, planUnitId, user.userId());
    ConfirmPlanUnitResponse response = ConfirmPlanUnitResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.OK, "일정이 확정되었습니다.", response);
  }

  // 일정 상세 조회 (모든 사용자)
  @GetMapping("/{planId}")
  public ApiResponse<GetPlanDetailResponse> getPlanDetail(@PathVariable UUID planId) {


    GetPlanDetailResult result = planQueryService.getPlanDetail(planId);
    GetPlanDetailResponse response = GetPlanDetailResponse.from(result);

    return ApiResponse.success(CommonSuccessCode.OK, "일정 상세 조회가 완료되었습니다.", response);
  }

  // 일정 목록 조회 (모든 사용자)
  @GetMapping
  public ApiResponse<Slice<GetPlanResponse>> getPlan(
      @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable,
      @ModelAttribute PlanSearchCondition planSearchCondition // 검색 조건 dto 의도 명확
  ) {
    Slice<GetPlanResponse> response = planQueryService.getPlan(
        GetPlanCommand.toCommand(pageable, planSearchCondition));
    return ApiResponse.success(CommonSuccessCode.OK, "일정 목록 조회가 완료되었습니다.", response);
  }


}
