package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import java.time.LocalDate;

public record PlanSearchCondition(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    RecruitStatus recruitStatus
) {

}
