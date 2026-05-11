package com.yeoljeong.tripmate.application.dto.command;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.presentation.dto.request.PlanSearchCondition;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public record GetPlanCommand(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    RecruitStatus recruitStatus,
    Pageable pageable
) {

  public static GetPlanCommand toCommand(Pageable pageable, PlanSearchCondition planSearchCondition) {
    return new GetPlanCommand(
        planSearchCondition.title(),
        planSearchCondition.startDate(),
        planSearchCondition.endDate(),
        planSearchCondition.recruitStatus(),
        pageable
    );
  }
}
