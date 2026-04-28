package com.yeoljeong.tripmate.domain.model.plan;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.domain.enums.Country;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 도/주 , 시
 * - 255자를 초과할 수 없다.
 * */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductAddress {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Country country; // 국가 (JP)

  @Column(nullable = false)
  private String state; // 도/주

  @Column(nullable = false)
  private String city; // 시


  public ProductAddress(Country country, String state, String city) {
    validateCountry(country);
    validateState(state);
    validateCity(city);
    this.country = country;
    this.state = state.trim();
    this.city = city.trim();
  }

  private void validateCity(String city) {
    if (city == null || city.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_CITY_REQUIRED);
    }

    String trimmedCity = city.trim();
    if (trimmedCity.length() > 255) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_CITY_EXCEED);
    }
  }

  private void validateState(String state) {
    if (state == null || state.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_STATE_REQUIRED);
    }

    String trimmedState = state.trim();
    if (trimmedState.length() > 255) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_STATE_EXCEED);
    }
  }

  private void validateCountry(Country country) {
    if (country == null) {
      throw new BusinessException(PlanErrorCode.PLAN_PRODUCT_SNAPSHOT_COUNTRY_REQUIRED);
    }
  }
}
