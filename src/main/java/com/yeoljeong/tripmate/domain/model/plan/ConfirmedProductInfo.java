package com.yeoljeong.tripmate.domain.model.plan;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.enums.Country;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmedProductInfo {
  @Column(name = "product_id", nullable = false)
  private UUID productId; // 상품 ID

  @Column(name = "product_name", nullable = false, length = 100)
  private String productName; // 상품명

  @Embedded
  private ProductAddress productAddress; // 주소(국가, 도/주, 시, 상세주소)

  @Column(name = "product_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal productPrice; // 가격


  public ConfirmedProductInfo(UUID productId, String name, Country country, String state, String city
      , BigDecimal price) {
    validateProductId(productId);
    validateName(name);
    validatePrice(price);

    ProductAddress address = new ProductAddress(country, state, city);

    this.productId = productId;
    this.productName = name.trim();
    this.productAddress = address;
    this.productPrice = price;
  }

  private void validatePrice(BigDecimal price) {
    if (price == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_PRICE_REQUIRED);
    }
    if (price.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_PRICE_NEGATIVE);
    }
  }

  private void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_NAME_REQUIRED);
    }

    String TrimmedName = name.trim();
    if (TrimmedName.length() > 100) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_NAME_EXCEED);
    }
  }

  private void validateProductId(UUID productId) {
    if (productId == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_ID_REQUIRED);
    }
  }
}
