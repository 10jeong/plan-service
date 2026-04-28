package com.yeoljeong.tripmate.domain.model.plan;

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

  // 일정에서 생성
  public ParticipantCount(int maxCount) {
    validatePositive(maxCount);

    this.maxCount = maxCount;
    this.currentCount = 1;
  }

  // 상품 스냅샷에서 생성
  public ParticipantCount(int maxCount, int currentCount) {
    validatePositive(maxCount);
    validatePositive(currentCount);
    validateNotExceed(maxCount, currentCount);

    this.maxCount = maxCount;
    this.currentCount = currentCount;
  }

  private void validateNotExceed(int maxCount, int currentCount) {
    if (currentCount > maxCount) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_EXCEED);
    }
  }

  private void validatePositive(int count) {
    if (count < 1) {
      throw new BusinessException(PlanErrorCode.PLAN_UNIT_PARTICIPANT_COUNT_NEGATIVE);
    }
  }
}
