package com.yeoljeong.tripmate.application.service.query;

import com.yeoljeong.tripmate.application.dto.command.GetPlanCommand;
import com.yeoljeong.tripmate.application.dto.external.ProductSummaryData;
import com.yeoljeong.tripmate.application.dto.external.UserData;
import com.yeoljeong.tripmate.application.dto.result.PlanParticipantDetail;
import com.yeoljeong.tripmate.application.dto.result.PlanUnitDetailResult;
import com.yeoljeong.tripmate.application.port.ProductReader;
import com.yeoljeong.tripmate.application.port.UserReader;
import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanRepository;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.exception.BusinessException;
import com.yeoljeong.tripmate.application.dto.result.GetPlanDetailResult;
import com.yeoljeong.tripmate.infrastructer.external.user.GetUserRequest;
import com.yeoljeong.tripmate.presentation.dto.response.GetPlanResponse;
import com.yeoljeong.tripmate.presentation.dto.response.MyParticipationRequestsResponse;
import com.yeoljeong.tripmate.presentation.dto.response.MyParticipationSummary;
import com.yeoljeong.tripmate.presentation.dto.response.PlanUnitParticipationSummary;
import com.yeoljeong.tripmate.presentation.dto.response.ReceivedParticipationRequestsResponse;
import com.yeoljeong.tripmate.presentation.dto.response.ReceivedParticipationSummary;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanQueryService {

  private final PlanRepository planRepository;
  private final PlanUnitRepository planUnitRepository;
  private final PlanParticipationRepository participationRepository;
  private final ProductReader productReader;
  private final UserReader userReader;

  public GetPlanDetailResult getPlanDetailFromDb(UUID planId) {
    log.info("상세조회 DB/Feign 로직 실행 planId={}", planId);

    Plan plan = planRepository.findById(planId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_NOT_FOUND));

    List<PlanUnit> planUnit = planUnitRepository.findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(plan);

    // 상품 정보 조회
    List<UUID> productScheduleIds = planUnit.stream().map(PlanUnit::getProductScheduleId).toList();
    List<ProductSummaryData> productSummaryData = productReader.getProductList(productScheduleIds);
    if (productSummaryData == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_NOT_FOUND);
    }
    Map<UUID, ProductSummaryData> productMap = productSummaryData.stream()
        .collect(Collectors.toMap(ProductSummaryData::productScheduleId,
            Function.identity()));

    boolean hasMissingProduct = productScheduleIds.stream()
        .anyMatch(scheduleId -> !productMap.containsKey(scheduleId));
    if (hasMissingProduct) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_NOT_FOUND);
    }

    List<PlanParticipation> planParticipation = participationRepository.findAllByPlanUnitIn(planUnit);

    List<UUID> userIds = planParticipation.stream()
        .map(PlanParticipation::getUserId)
        .toList();

    // 회원 정보 조회
    Map<UUID, UserData> userMap = getUserMap(userIds);

    // 참여자 그룹핑
    Map<PlanUnit, List<PlanParticipantDetail>> participationMap = planParticipation.stream()
        .collect(Collectors.groupingBy(PlanParticipation::getPlanUnit,
            Collectors.mapping(
                participation ->{
                    UserData user = userMap.get(participation.getUserId());
                  return PlanParticipantDetail.from(user, participation);
                },
                Collectors.toList()
            )
        ));

    List<PlanUnitDetailResult> planUnitResult =
        planUnit.stream()
            .map(unit ->
              PlanUnitDetailResult.from(unit, productMap.get(unit.getProductScheduleId()), participationMap.getOrDefault(unit, List.of())
              )).toList();
    return GetPlanDetailResult.from(plan, planUnitResult);

  }


  public Slice<GetPlanResponse> getPlan(GetPlanCommand command) {
    String title = command.title() == null || command.title().isBlank()
        ? null
        : "%" + command.title().trim() + "%";

    RecruitStatus recruitStatus = command.recruitStatus() == null
        ? RecruitStatus.OPEN
        : command.recruitStatus();

    Slice<Plan> plans = planRepository.findByCondition(
        title,
        command.startDate(),
        command.endDate(),
        recruitStatus,
        command.pageable()
    );

    return plans.map(GetPlanResponse::from);
  }

  public Slice<MyParticipationRequestsResponse> getMyParticipationRequests(Pageable pageable, UUID userId) {
    Pageable planPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    Slice<Plan> planSlice = planRepository.findMyParticipatedPlans(userId, planPageable);

    List<PlanParticipation> participations = participationRepository.findAllByUserIdAndPlanUnit_PlanInAndParticipationRoleAndIsDeletedFalse(
        userId, planSlice.getContent(), ParticipationRole.GUEST);

    Map<Plan, List<PlanParticipation>> groupedByPlan = participations.stream()
        .collect(Collectors.groupingBy(
            p -> p.getPlanUnit().getPlan()
        ));

    List<MyParticipationRequestsResponse> response = groupedByPlan.entrySet().stream()
        .map(
            entry -> {
              Plan plan = entry.getKey();
              List<MyParticipationSummary> participationSummaries = entry.getValue().stream()
                  .map(participation -> {
                    PlanUnit planUnit = participation.getPlanUnit();
                    return MyParticipationSummary.from(planUnit, participation);
                  }).toList();
              return MyParticipationRequestsResponse.from(plan, participationSummaries);
            }).toList();

    return new SliceImpl<>(
        response,
        pageable,
        planSlice.hasNext()
    );
  }

  public Slice<ReceivedParticipationRequestsResponse> getReceivedParticipationRequests(
      Pageable pageable, UUID userId) {
    Pageable planPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    Slice<Plan> planSlice = planRepository.findHostPlans(userId, planPageable);

    List<PlanParticipation> participations = participationRepository.findGuestRequestsByPlans(planSlice.getContent());

    Map<Plan, List<PlanParticipation>> groupedByPlan = participations.stream()
        .collect(Collectors.groupingBy(
            p -> p.getPlanUnit().getPlan()
        ));

    // 사용자 정보 조회
    List<UUID> userIds = participations.stream()
        .map(PlanParticipation::getUserId).toList();
    Map<UUID, UserData> userDataMap = getUserMap(userIds);

    List<ReceivedParticipationRequestsResponse> response = groupedByPlan.entrySet().stream()
        .map(
            planEntry -> {
              Plan plan = planEntry.getKey();

              // Plan 내부에서 다시 PlanUnit 기준 그룹핑
              Map<PlanUnit, List<PlanParticipation>> groupedByPlanUnit =
                  planEntry.getValue().stream()
                      .collect(Collectors.groupingBy(
                          PlanParticipation::getPlanUnit
                      ));

              List<PlanUnitParticipationSummary> planUnits = groupedByPlanUnit.entrySet().stream()
                  .map(
                      unitEntry -> {
                        PlanUnit planUnit = unitEntry.getKey();
                        List<ReceivedParticipationSummary> applicants = unitEntry.getValue().stream()
                            .map(
                                participation -> {
                                  UserData userData = userDataMap.get(participation.getUserId());
                                  return ReceivedParticipationSummary.from(participation, userData);
                                }
                            ).toList();
                        return PlanUnitParticipationSummary.from(applicants, planUnit);
                      }
                  ).toList();
              return ReceivedParticipationRequestsResponse.from(plan, planUnits);
            }).toList();

    return new SliceImpl<>(
        response,
        pageable,
        planSlice.hasNext()
    );

  }

  /* 유저 정보 조회 및 검증*/
  private Map<UUID, UserData> getUserMap(List<UUID> userIds) {
    List<UserData> userData = userIds.isEmpty() ?List.of() : userReader.getUser(
        GetUserRequest.from(userIds));
    if (userData == null) {
      throw new BusinessException(PlanErrorCode.USER_NOT_FOUND);
    }
    Map<UUID, UserData> userMap = userData.stream()
        .collect(Collectors.toMap(UserData::userId,
            Function.identity(),
            (left, right) -> right));

    boolean hasMissingUser = userIds.stream()
        .anyMatch(userId -> !userMap.containsKey(userId));
    if (hasMissingUser) {
      throw new BusinessException(PlanErrorCode.USER_NOT_FOUND);
    }
    return userMap;
  }


}
