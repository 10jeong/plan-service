package com.yeoljeong.tripmate.domain.enums;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.exception.BusinessException;
import java.util.Arrays;

public enum PlanCreationType {
  CUSTOM;

  public static PlanCreationType from(String planType) {
    if (planType == null || planType.isBlank()) {
      throw new BusinessException(PlanErrorCode.PLAN_TYPE_REQUIRED);
    }
    return Arrays.stream(values())
        .filter(type -> type.name().equalsIgnoreCase(planType.trim()))
        .findFirst()
        .orElseThrow(() -> new BusinessException(PlanErrorCode.PLAN_CREATION_TYPE_INVALID));
  }
}
