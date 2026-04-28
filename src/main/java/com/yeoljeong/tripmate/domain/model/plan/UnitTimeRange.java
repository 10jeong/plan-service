package com.yeoljeong.tripmate.domain.model.plan;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
* 각 필드의 값은 필수이다.
* 예상 시작 시간은 예상 종료 시간 이전이어야한다.
* */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UnitTimeRange {

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime; // 예상 시작 시간

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime; // 예상 종료 시간


  public UnitTimeRange(LocalTime startTime, LocalTime endTime) {
    if (startTime == null || endTime == null) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_REQUIRED);
    }

    if (!startTime.isBefore(endTime)) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_TIME_RANGE_INVALID);
    }

    this.startTime = startTime;
    this.endTime = endTime;
  }
}
