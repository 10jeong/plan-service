package com.yeoljeong.tripmate.domain.participation;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.participation.enums.ParticipantRole;
import com.yeoljeong.tripmate.domain.participation.enums.ParticipantStatus;
import com.yeoljeong.tripmate.domain.plan.PlanUnit;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_plan_participation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanParticipation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
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

  public PlanParticipation(UUID userId, ParticipantRole participantRole, PlanUnit planUnit) {
    validateUserId(userId);
    validateParticipantRole(participantRole);
    validatePlanUnit(planUnit);

    this.userId = userId;
    this.participantRole = participantRole;
    this.participantStatus = ParticipantStatus.PENDING;
    this.planUnit = planUnit;
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
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_REQUIRED);
    }
  }

}
