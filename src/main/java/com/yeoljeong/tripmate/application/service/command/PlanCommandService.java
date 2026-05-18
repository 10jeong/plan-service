package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.command.CreatePlanCommand;
import com.yeoljeong.tripmate.application.dto.command.CreatePlanUnitCommand;
import com.yeoljeong.tripmate.application.dto.command.ParticipatePlanCommand;
import com.yeoljeong.tripmate.application.dto.command.UpdateParticipationStatusCommand;
import com.yeoljeong.tripmate.application.dto.command.WithdrawPlanUnitParticipationCommand;
import com.yeoljeong.tripmate.application.dto.result.ConfirmPlanUnitResult;
import com.yeoljeong.tripmate.application.dto.result.CreatePlanResult;
import com.yeoljeong.tripmate.application.dto.result.ParticipatePlanResult;
import com.yeoljeong.tripmate.application.dto.result.UpdateParticipationStatusResult;
import com.yeoljeong.tripmate.application.port.PlanEvents;
import com.yeoljeong.tripmate.domain.enums.Country;
import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.model.PlanProductSnapshot;
import com.yeoljeong.tripmate.application.dto.external.ProductData;
import com.yeoljeong.tripmate.application.port.ProductReader;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import com.yeoljeong.tripmate.domain.repository.PlanProductSnapshotRepository;
import com.yeoljeong.tripmate.domain.repository.PlanRepository;
import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import com.yeoljeong.tripmate.domain.repository.PlanUnitRepository;
import com.yeoljeong.tripmate.event.EventUtils;
import com.yeoljeong.tripmate.event.PlanUnitConfirmedEvent;
import com.yeoljeong.tripmate.event.PlanUnitParticipantQuitEvent;
import com.yeoljeong.tripmate.exception.BusinessException;
import com.yeoljeong.tripmate.presentation.dto.response.WithdrawPlanUnitParticipationResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanCommandService {

  private final PlanRepository planRepository;
  private final PlanUnitRepository planUnitRepository;
  private final PlanParticipationRepository planParticipationRepository;
  private final PlanProductSnapshotRepository planProductSnapshotRepository;
  private final ProductReader productReader;
  private final PlanEvents events;

  /*
  * 일정 생성(일정, 단위일정, 참여)
  * */
  public CreatePlanResult createPlans(CreatePlanCommand command) {

    List<CreatePlanUnitCommand> requestPlanUnits = command.getPlanUnit();
    validateRequestPlanUnits(requestPlanUnits);

    // 동일한 일정 내 [일차, 순서] 존재 x
    validateDuplicateDayAndOrder(command.getPlanUnit());

    // 동일한 일정 내 단위 일정 시간 범위가 겹치지 않는지 검증
    validateUnitTimeRangeNotOverlap(command.getPlanUnit());

    /* 일정 생성 */
    Plan plan = Plan.builder()
        .title(command.getTitle())
        .description(command.getDescription())
        .startDate(command.getStartDate())
        .endDate(command.getEndDate())
        .planType(command.getPlanType())
        .build();
    Plan savedPlan = planRepository.save(plan);

    /* 단위 일정 생성 */
    List<PlanUnit> planUnit = command.getPlanUnit().stream()
        .map(unitCommand -> PlanUnit.builder()
            .day(unitCommand.getDay())
            .orderIndex(unitCommand.getOrderIndex())
            .title(unitCommand.getTitle())
            .description(unitCommand.getDescription())
            .startTime(unitCommand.getStartTime())
            .endTime(unitCommand.getEndTime())
            .price(unitCommand.getPrice())
            .maxCount(unitCommand.getMaxCount())
            .plan(savedPlan)
            .productScheduleId(unitCommand.getProductScheduleId())
            .build())
        .toList();

    planUnitRepository.saveAll(planUnit);

    /* 침여 생성 */
    List<PlanParticipation> planParticipation = planUnit.stream()
        .map(unit -> PlanParticipation.createHost(command.getHostId(),unit))
        .toList();

    planParticipationRepository.saveAll(planParticipation);

    return CreatePlanResult.from(savedPlan);

  }

  /*
  * 참여 신청
  * */
  @CacheEvict(
      cacheNames = "planDetail",
      key = "#planId"
  )
  public ParticipatePlanResult participatePlanUnit(ParticipatePlanCommand command) {

    PlanUnit planUnit = getPlanUnitInPlan(command.planId(), command.planUnitId());

    // 중복 참여 검증
    validateDuplicateParticipation(command.planUnitId(),command.guestId());
    // 모집 가능한 인원인지(최대 참여 인원 초과 여부) 검증
    try {
      PlanParticipation participation = PlanParticipation.createGuest(command.guestId(), planUnit);
      PlanParticipation savedParticipation = planParticipationRepository.save(participation);

      return ParticipatePlanResult.from(
          savedParticipation.getId(),
          savedParticipation.getPlanUnit().getTitle(),
          savedParticipation.getUpdatedAt(),
          savedParticipation.getUpdatedBy());
    } catch (DataIntegrityViolationException e) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_ALREADY_EXISTS);
    }
  }

  /*
  * 참여 신청 상태 호스트 변경(REQUESTED -> APPROVED/REJECTED)
  * */
  public UpdateParticipationStatusResult updateParticipationStatus(
      UpdateParticipationStatusCommand command) {
    
    PlanUnit planUnit = getPlanUnitInPlan(command.planId(), command.planUnitId());

    // 요청자가 해당 단위 일정의 역할이 'HOST'인지 검증하기 위한 객체
    PlanParticipation hostPlanParticipation = planParticipationRepository.findByPlanUnitAndUserId(
            planUnit, command.userId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    hostPlanParticipation.validateHostOf(planUnit);


    // 상태를 변경을 위한 객체
    PlanParticipation targetPlanParticipation = planParticipationRepository.findByIdAndPlanUnit(
            command.participationId(),planUnit)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    // 상태 변경 검증
    targetPlanParticipation.validatePlanParticipationStatus(command.status());
    // 상태 변경
    targetPlanParticipation.changeStatus(command.status());

    return UpdateParticipationStatusResult.from(targetPlanParticipation);
  }

  /*
  * 일정 확정
  * */
  public ConfirmPlanUnitResult confirmPlanUnit(UUID planId, UUID planUnitId, UUID userId)
      throws NoSuchAlgorithmException {

    PlanUnit planUnit = getPlanUnitInPlan(planId, planUnitId);

    validatePlanUnitHost(planUnit, userId);

    planUnit.confirmPlanUnit();

    // 스냅샷 저장 (상품 정보 API + 최대인원, 현재인원)
    ProductData productData = productReader.getProduct(planUnit.getProductScheduleId());
    if (productData == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_NOT_FOUND);
    }
    
    PlanProductSnapshot planProductSnapshot = PlanProductSnapshot.builder()
        .productScheduleId(productData.productScheduleId())
        .name(productData.productName())
        .country(Country.valueOf(productData.country()))
        .state(productData.state())
        .city(productData.city())
        .price(productData.price())
        .maxCount(planUnit.getParticipantCount().getMaxCount())
        .currentCount(planUnit.getParticipantCount().getCurrentCount())
        .planUnit(planUnit)
        .build();

    planProductSnapshotRepository.save(planProductSnapshot);

    // 알림 수신자
    List<UUID> receivers = planParticipationRepository.findAllByPlanUnitAndParticipationStatus(
        planUnit, ParticipationStatus.PAID)
        .stream()
        .map(PlanParticipation::getUserId)
        .toList();

    events.planUnitConfirmed(new PlanUnitConfirmedEvent(
        EventUtils.getEventHash("planUnit", String.valueOf(planUnitId), planUnit.getUpdatedAt()),
        planUnitId,
        planUnit.getTitle(),
        receivers));

    return ConfirmPlanUnitResult.from(planUnitId, planUnit.getTitle(), planUnit.isConfirmed(),
        planUnit.getUpdatedAt(), planUnit.getUpdatedBy());

  }

  /*
  * 참여 일정 탈퇴
  * : 참여 상태가 CONFIRMED인 사용자만 가능
  * */
  public WithdrawPlanUnitParticipationResponse withdrawPlanUnitParticipant(
      WithdrawPlanUnitParticipationCommand command) {
    PlanUnit planUnit = planUnitRepository.findById(command.planUnitId())
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));

    PlanParticipation participation = planParticipationRepository.findByIdAndPlanUnit(
            command.participationId(), planUnit)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_FOUND));

    participation.withdraw(command.userId());
    planUnitRepository.deductParticipantCount(planUnit.getId(), 1);

    // 이벤트 발행
    events.planUnitParticipationQuit(UUID.randomUUID(), command.userId(), planUnit.getId(), command.reason());

    return WithdrawPlanUnitParticipationResponse.from(participation);
  }

  private void validatePlanUnitHost(PlanUnit planUnit, UUID userId) {
    boolean isHost = planParticipationRepository.existsByPlanUnitAndUserIdAndParticipationRole(planUnit, userId,
        ParticipationRole.HOST);

    if (!isHost) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_HOST);
    }
  }


  private void validateDuplicateParticipation(UUID planUnitId, UUID guestId) {
    if (planParticipationRepository.existsByPlanUnitIdAndUserIdAndIsDeletedIsFalse(planUnitId, guestId)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_ALREADY_EXISTS);
    }
  }

  private PlanUnit getPlanUnitInPlan(UUID planId, UUID planUnitId) {
    if (!planRepository.existsById(planId)) {
      throw new BusinessException(PlanErrorCode.PLAN_NOT_FOUND);
    }
    return planUnitRepository.findByIdAndPlanId(planUnitId, planId)
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_UNIT_NOT_FOUND));
  }

  private void validateRequestPlanUnits(List<CreatePlanUnitCommand> requestPlanUnits) {
    if (requestPlanUnits == null || requestPlanUnits.isEmpty()) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_REQUIRED);
    }

    for (CreatePlanUnitCommand unit : requestPlanUnits) {
      if (unit == null) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_REQUIRED);
      }
      if (unit.getStartTime() == null || unit.getEndTime() == null) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_REQUIRED);
      }
      if (!unit.getStartTime().isBefore(unit.getEndTime())) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_INVALID);
      }
    }
  }

  private void validateUnitTimeRangeNotOverlap(List<CreatePlanUnitCommand> planUnit) {
    // 일차, 예상시작시간의 순서대로 정렬
    List<CreatePlanUnitCommand> sortedPlanUnits = planUnit.stream()
        .sorted(
            Comparator.comparingInt(CreatePlanUnitCommand::getDay)
                .thenComparing(CreatePlanUnitCommand::getStartTime)
        )
        .toList();

    for(int i=0; i<sortedPlanUnits.size()-1; i++) {
      CreatePlanUnitCommand previous = sortedPlanUnits.get(i);
      CreatePlanUnitCommand current = sortedPlanUnits.get(i+1);
      int previousDay = previous.getDay();
      int currentDay = current.getDay();

      // 일차가 다르다면 비교 x
      if (previousDay != currentDay) continue;

      // 일차가 같다면 이전 순서의 종료시간과 이후 순서의 시작시간 비교
      if (previous.getEndTime().isAfter(current.getStartTime())) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_OVERLAP);
      }
    }
  }

  private void validateDuplicateDayAndOrder(List<CreatePlanUnitCommand> planUnit) {
    Set<String> keys = new HashSet<>();

    for (CreatePlanUnitCommand unit : planUnit) {
      String key = unit.getDay() + ", " + unit.getOrderIndex();

      if (!keys.add(key)) {
        throw new BusinessException(PlanErrorCode.PLAN_UNIT_DUPLICATED_ORDER);
      }
    }
  }


}
