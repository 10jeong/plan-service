package com.yeoljeong.tripmate.application.dto.command;

import com.yeoljeong.tripmate.presentation.dto.request.withdrawPlanUnitParticipationRequest;
import java.util.UUID;

public record WithdrawPlanUnitParticipationCommand(
    String reason,
    UUID planId,
    UUID planUnitId,
    UUID participationId,
    UUID userId
) {

  public static WithdrawPlanUnitParticipationCommand from(withdrawPlanUnitParticipationRequest request, UUID planId, UUID planUnitId, UUID participationId, UUID userId) {
    return new WithdrawPlanUnitParticipationCommand(
        request.reason(),
        planId,
        planUnitId,
        participationId,
        userId
    );
  }
}
