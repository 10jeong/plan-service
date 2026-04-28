package com.yeoljeong.tripmate.domain.plan;

import jakarta.persistence.Column;
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

  @Column(name = "product_id", nullable = false)
  private UUID productId; // 상품 ID

  @Column(nullable = false, length = 100)
  private String name; // 상품명

  @Embedded
  private ProductAddress address; // 주소(국가, 도/주, 시, 상세주소)

  @Column(nullable = false)
  private BigDecimal price; // 가격

  // todo : planUnit의 vo를 재사용해도 되는가 고민
  @Embedded
  private ParticipantCount participantCount; // 참가인원(현재 인원, 최대 인원)

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_unit_id", nullable = false, unique = true)
  private PlanUnit planUnit; // 단위일정


  public PlanProductSnapshot(UUID productId, String name, ProductAddress address, BigDecimal price,
      ParticipantCount participantCount, PlanUnit planUnit) {
    this.productId = productId;
    this.name = name;
    this.address = address;
    this.price = price;
    this.participantCount = participantCount;
    this.planUnit = planUnit;
  }
}
