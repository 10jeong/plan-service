package com.yeoljeong.tripmate.domain.model.plan;

import com.yeoljeong.tripmate.domain.BaseAuditEntity;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.enums.ParticipantRole;
import com.yeoljeong.tripmate.domain.enums.ParticipantStatus;
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
  @Column(name = "participant_role", nullable = false)
  private ParticipantRole participantRole; // 역할(HOST, GUEST)

  @Enumerated(EnumType.STRING)
  @Column(name = "participant_status", nullable = false)
  private ParticipantStatus participantStatus = ParticipantStatus.PENDING; // 상태(PENDING, APPROVAL, REJECTED)

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_unit_id", nullable = false)
  private PlanUnit planUnit;

  public static PlanParticipation createHost(UUID userId, PlanUnit planUnit) {
    return new PlanParticipation(userId, ParticipantRole.HOST, ParticipantStatus.APPROVAL, planUnit);
  }

  public static PlanParticipation createGuest(UUID userId, PlanUnit planUnit) {
    return new PlanParticipation(userId, ParticipantRole.GUEST, ParticipantStatus.PENDING, planUnit);
  }

  public PlanParticipation(UUID userId, ParticipantRole participantRole, ParticipantStatus participantStatus, PlanUnit planUnit) {
    validateUserId(userId);
    validateParticipantRole(participantRole);
    validateParticipantStatus(participantStatus);
    validatePlanUnit(planUnit);

    this.userId = userId;
    this.participantRole = participantRole;
    this.participantStatus = participantStatus;
    this.planUnit = planUnit;
  }

  private void validateParticipantStatus(ParticipantStatus participantStatus) {
    if (participantStatus == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPANT_STATUS_REQUIRED);
    }
  }


  private void validateUserId(UUID userId) {
    if (userId == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPANT_USER_REQUIRED);
    }
  }

  private void validateParticipantRole(ParticipantRole participantRole) {
    if (participantRole == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPANT_ROLE_REQUIRED);
    }
  }

  private void validatePlanUnit(PlanUnit planUnit) {
    if (planUnit == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PARTICIPANT_PLAN_UNIT_REQUIRED);
    }
  }

}
