package com.yeoljeong.tripmate.presentation.dto.response;

import com.yeoljeong.tripmate.domain.model.PlanParticipation;
import java.util.UUID;

public record WithdrawPlanUnitParticipationResponse(
    UUID planParticipationId
) {

  public static WithdrawPlanUnitParticipationResponse from(PlanParticipation participation) {
    return new WithdrawPlanUnitParticipationResponse(participation.getId());
  }
}
