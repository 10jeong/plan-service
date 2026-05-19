package com.yeoljeong.tripmate.domain.model;
import com.yeoljeong.tripmate.domain.BaseAuditEntity;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.enums.PlanCreationType;
import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Plan extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title; // 제목

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description; // 설명

  @Column(nullable = false)
  private String imageUrl; // 이미지 url

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
  public Plan(String title, String description, LocalDate startDate, LocalDate endDate, String planType, String imageUrl) {
    validateTitle(title);
    validateDescription(description);
// todo: imageUrl validate
    this.title = title.trim();
    this.description = description.trim();
    this.travelPeriod = new TravelPeriod(startDate, endDate);
    this.planType = validatePlanType(planType);
    this.recruitStatus = RecruitStatus.OPEN;
    this.imageUrl = imageUrl;
  }

  private PlanCreationType validatePlanType(String planType) {
    return PlanCreationType.from(planType);
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
