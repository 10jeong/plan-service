package com.yeoljeong.tripmate.application.dto.result;

import com.yeoljeong.tripmate.domain.model.Plan;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreatePlanResult {
  private UUID planId;
  private String title;
  private LocalDateTime createdAt;
  private UUID createdBy;

  public static CreatePlanResult from(Plan plan) {
    return new CreatePlanResult(plan.getId(), plan.getTitle(), plan.getCreatedAt(), plan.getCreatedBy());
  }
}
