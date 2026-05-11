package com.yeoljeong.tripmate.domain.model;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
* 여행기간(시작일)이 현재일부터 가능하다.
* 여행기간(시작일)이 여행기간(종료일)보다 이전이여야한다.
* */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPeriod {
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  public TravelPeriod(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      throw new BusinessException(PlanErrorCode.PLAN_TRAVEL_PERIOD_REQUIRED);
    }

    if (startDate.isBefore(LocalDate.now()) || startDate.isAfter(endDate)) {
      throw new BusinessException(PlanErrorCode.PLAN_TRAVEL_PERIOD_INVALID);
    }

    this.startDate = startDate;
    this.endDate = endDate;
  }
}
