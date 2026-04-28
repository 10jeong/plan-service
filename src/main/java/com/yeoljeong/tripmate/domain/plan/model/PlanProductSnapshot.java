package com.yeoljeong.tripmate.domain.plan.model;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.plan.enums.Country;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_plan_product_snapshot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanProductSnapshot {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Embedded
  private ConfirmedProductInfo productInfo;

  // todo : planUnit의 vo를 재사용해도 되는가 고민
  @Embedded
  private ParticipantCount participantCount; // 참가인원(현재 인원, 최대 인원)

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_unit_id", nullable = false, unique = true)
  private PlanUnit planUnit; // 단위일정


  public PlanProductSnapshot(UUID productId, String name, Country country, String state, String city,
      BigDecimal price, ParticipantCount participantCount, PlanUnit planUnit) {

    ConfirmedProductInfo productInfo =  new ConfirmedProductInfo(productId, name, country, state, city, price);

    validatePlanUnit(planUnit);

    this.productInfo = productInfo;
    this.participantCount = participantCount;
    this.planUnit = planUnit;
  }

  private void validatePlanUnit(PlanUnit planUnit) {
    if (planUnit == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_PLAN_UNIT_REQUIRED);
    }
  }


}
