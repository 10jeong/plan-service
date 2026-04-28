package com.yeoljeong.tripmate.domain.plan;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.plan.enums.PlanCreationType;
import com.yeoljeong.tripmate.domain.plan.enums.RecruitStatus;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Plan {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title; // 제목

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description; // 설명

  @Embedded
  private TravelPeriod travelPeriod; // 여행기간

  @Enumerated(EnumType.STRING)
  @Column(name = "plan_type", nullable = false)
  private PlanCreationType planType; // 생성방식(CUSTOM)

  @Enumerated(EnumType.STRING)
  @Column(name = "recruit_status", nullable = false)
  private RecruitStatus recruitStatus; // 모집 상태(OPEN, CLOSE)

  /*
  *  여행 일정 생성
  * 제목
  *  - 255자를 초과할 수 없다.
  * */
  @Builder
  public Plan(String title, String description, LocalDate startDate, LocalDate endDate, PlanCreationType planType) {
    validateTitle(title);
    validateDescription(description);
    validatePlanType(planType);

    TravelPeriod planPeriod = new TravelPeriod(startDate, endDate);

    this.title = title.trim();
    this.description = description.trim();
    this.travelPeriod = planPeriod;
    this.planType = planType;
    this.recruitStatus = RecruitStatus.OPEN;
  }

  private void validatePlanType(PlanCreationType planType) {
    if (planType == null) {
      throw new BusinessException(PlanErrorCode.PLAN_TYPE_REQUIRED);
    }
  }

  private void validateDescription(String description) {
    if (description == null || description.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_DESCRIPTION_REQUIRED);
    }
  }

  private void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_TITLE_REQUIRED);
    }

    String trimmedTitle = title.trim();

    if (trimmedTitle.length() > 255) {
      throw new BusinessException(PlanErrorCode.PLAN_TITLE_EXCEED);
    }
  }
}
