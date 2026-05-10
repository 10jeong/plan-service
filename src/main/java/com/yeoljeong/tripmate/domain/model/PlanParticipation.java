package com.yeoljeong.tripmate.domain.model;

import com.yeoljeong.tripmate.domain.BaseAuditEntity;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.enums.ParticipationRole;
import com.yeoljeong.tripmate.domain.enums.ParticipationStatus;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "p_plan_participation",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_plan_particiation_user_plan_unit",
            columnNames = {"user_id", "plan_unit_id"}
        )
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlanParticipation extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId; // 사용자 id

  @Enumerated(EnumType.STRING)
  @Column(name = "participation_role", nullable = false)
  private ParticipationRole participationRole; // 역할(HOST, GUEST)

  @Enumerated(EnumType.STRING)
  @Column(name = "participation_status", nullable = false)
  private ParticipationStatus participationStatus = ParticipationStatus.REQUESTED; // 상태(REQUESTED, APPROVED, REJECTED, RESERVED, PARTICIPATION_CONFIRMED, RESERVED_CANCELLED, PAYMENT_CANCELLED)

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_unit_id", nullable = false)
  private PlanUnit planUnit;

  public static PlanParticipation createHost(UUID userId, PlanUnit planUnit) {
    return new PlanParticipation(userId, ParticipationRole.HOST, ParticipationStatus.APPROVED, planUnit);
  }

  public static PlanParticipation createGuest(UUID userId, PlanUnit planUnit) {
    return new PlanParticipation(userId, ParticipationRole.GUEST, ParticipationStatus.REQUESTED, planUnit);
  }

  public PlanParticipation(UUID userId, ParticipationRole participationRole, ParticipationStatus participationStatus, PlanUnit planUnit) {
    validateUserId(userId);
    validateParticipationRole(participationRole);
    validateParticipationStatus(participationStatus);
    validatePlanUnit(planUnit);

    this.userId = userId;
    this.participationRole = participationRole;
    this.participationStatus = participationStatus;
    this.planUnit = planUnit;
  }

  /*
  * 일정 참여상태 변경 검증
  * */
  public void validatePlanParticipationStatus(ParticipationStatus next) {
    if (next == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_REQUIRED);
    }
    if (!this.participationStatus.canChangeTo(next)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID);
    }
  }

  /*
   * 주문 생성시, 참여 상태 검증
   * */
  public void validateApprovalStatus() {
    if (this.participationStatus != ParticipationStatus.APPROVED) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_NOT_APPROVAL);
    }
  }

  /*
  * 주문 생성시, 참여 상태(APPROVED -> RESERVED)
  * */
  public void confirmParticipation() {
    validateApprovalStatus();
    this.participationStatus = ParticipationStatus.RESERVED;
  }

  /*
  * 일정 참여상태 변경
  * */
  public void changeStatus(ParticipationStatus next) {
    if (next == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_REQUIRED);
    }
    if (!this.participationStatus.canChangeTo(next)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_CHANGE_INVALID); // -> 에러코드 메시지 바꾸기
    }
    this.participationStatus = next;
  }

  /*
  * 참여 합류된 일정 탈퇴
  * */
  public void withdraw(UUID userId) {
    validateOwner(userId);
    validateDeleted();
    validatePlanParticipationStatus(ParticipationStatus.PAYMENT_CANCELLED);

    this.participationStatus = ParticipationStatus.PAYMENT_CANCELLED;
    this.softDelete();
  }

  private void validateDeleted() {
    if (this.isDeleted) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_ALREADY_DELETED);
    }
  }

  private void validateOwner(UUID userId) {
    if (!this.userId.equals(userId)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_FORBIDDEN);
    }
  }


  public void validateHostOf(PlanUnit planUnit) {

    if (!this.planUnit.equals(planUnit)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_PLAN_UNIT_MISMATCH);
    }

    // host 검증
    if (!this.participationRole.equals(ParticipationRole.HOST)) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_NOT_HOST);
    }
  }

  private void validateParticipationStatus(ParticipationStatus participationStatus) {
    if (participationStatus == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_STATUS_REQUIRED);
    }
  }


  private void validateUserId(UUID userId) {
    if (userId == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_USER_REQUIRED);
    }
  }

  private void validateParticipationRole(ParticipationRole participationRole) {
    if (participationRole == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_ROLE_REQUIRED);
    }
  }

  private void validatePlanUnit(PlanUnit planUnit) {
    if (planUnit == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPATION_PLAN_UNIT_REQUIRED);
    }
  }

}
