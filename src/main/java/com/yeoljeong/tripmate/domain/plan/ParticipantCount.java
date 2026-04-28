package com.yeoljeong.tripmate.domain.plan;

import com.yeoljeong.tripmate.domain.exception.PlanErrorCode;
import com.yeoljeong.tripmate.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/*
* 참여 현재 인원은 참여 최대 인원을 초과할 수 없다.
* 각 필드의 값은 1이상이다.
* */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantCount {

  @Column(name = "max_count", nullable = false)
  private int maxCount = 1; // 참여 최대 인원

  @Column(name = "current_count", nullable = false)
  private int currentCount = 1; // 참여 현재 인원

  public ParticipantCount(int maxCount) {
    validatePositive(maxCount);
    validateNotExceed(maxCount);

    this.maxCount = maxCount;
    this.currentCount = 1;
  }

  private void validateNotExceed(int maxCount) {
    if (currentCount > maxCount) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_EXCEED);
    }
  }

  private void validatePositive(int maxCount) {
    if (maxCount < 1) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_MAX_COUNT_INVALID);
    }
  }
}
