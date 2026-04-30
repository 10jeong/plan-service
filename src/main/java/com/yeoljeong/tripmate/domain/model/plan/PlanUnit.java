package com.yeoljeong.tripmate.domain.model.plan;

import com.yeoljeong.tripmate.domain.BaseAuditEntity;
import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "p_plan_unit",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_plan_unit_plan_day_order",
            columnNames = {"plan_id", "day", "order_index"}
        )
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlanUnit extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private int day; // 일차

  @Column(name = "order_index", nullable = false)
  private int orderIndex; // 순서

  @Column(nullable = false)
  private String title; // 제목

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description; // 설명

  @Embedded
  private UnitTimeRange unitTimeRange; // 예상소요시간

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price; // 가격

  @Embedded
  private ParticipantCount participantCount; // 참가인원(최대,현재)

  @Column(name = "is_confirmed", nullable = false)
  private boolean isConfirmed = false; // 확정여부

  @Column(name = "product_id", nullable = false)
  private UUID productId; // 상품 ID

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_id", nullable = false)
  private Plan plan; // 일정 ID


  /*
  * 단위 일정 생성
  * - todo: 서비스에서 검증 - 동일한 [일차, 순서]는 존재햐지 않는다.
  * - todo: 서비스에서 검증 - 예상소요시간은 이전의 순서와의 시간순을 확인해야한다.
  * - todo: 상품 ID를 검증해야할지 고민
  * 차수
  * - 값이 1 이상이다.
  * 순서
  * - 값이 1 이상이다.
  * 제목
  * - 255자 이하다.
  * 가격
  * - 값이 0 이상이다.
  * 참가인원
  * - 확정여부는 기본값으로 FALSE 이다.
  * */
  @Builder
  public PlanUnit(int day, int orderIndex, String title, String description,
      LocalTime startTime, LocalTime endTime, BigDecimal price, int maxCount,
      Plan plan, UUID productId) {
    validateDay(day);
    validateOrderIndex(orderIndex);
    validateTitle(title);
    validateDescription(description);
    validatePrice(price);
    validatePlan(plan);
    validateProductId(productId);

    UnitTimeRange unitTime = new UnitTimeRange(startTime, endTime);
    ParticipantCount participant = new ParticipantCount(maxCount);

    this.day = day;
    this.orderIndex = orderIndex;
    this.title = title.trim();
    this.description = description.trim();
    this.unitTimeRange = unitTime;
    this.price = price;
    this.participantCount = participant;
    this.isConfirmed = false;
    this.plan = plan;
    this.productId = productId;
  }

  /* 일정 확정*/
  public void confirmPlanUnit() {
    if (this.isConfirmed) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_ALREADY_CONFIRMED);
    }
    this.isConfirmed = true;
  }

  private void validateOrderIndex(int orderIndex) {
    if (orderIndex < 1) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_ORDER_INVALID);
    }
  }

  private void validateDay(int day) {
    if (day < 1) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_DAY_INVALID);
    }
  }

  private void validateProductId(UUID productId) {
    if (productId == null) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PRODUCT_REQUIRED);
    }
  }

  private void validatePlan(Plan plan) {
    if (plan == null) {
      throw new BusinessException(PlanErrorCode.PLAN_REQUIRED);
    }
  }


  private void validatePrice(BigDecimal price) {
    if (price == null) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PRICE_REQUIRED);
    }

    if (price.compareTo(BigDecimal.ZERO) < 0) { // price가 음수인 경우
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PRICE_INVALID);
    }

    if (price.stripTrailingZeros().scale() > 2) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PRICE_SCALE_INVALID);
    }
  }

  private void validateDescription(String description) {
    if (description == null || description.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_DESCRIPTION_REQUIRED);
    }
  }

  private void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_TITLE_REQUIRED);
    }

    String trimmedTitle = title.trim();
    if (trimmedTitle.length() > 255) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_TITLE_EXCEED);
    }
  }


}
