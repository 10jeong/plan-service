package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.command.FindParticipantStatusCommand;
import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanInternalCommandService {

  private final PlanParticipationRepository planParticipationRepository;

  public FindParticipationStatusResult findParticipationStatusByPlanUnitIdAndUserId(
      FindParticipantStatusCommand command) {

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(command.planUnitId(), command.userId());

    return new FindParticipationStatusResult(command.planUnitId(), command.userId(),
        participation.getParticipationStatus());
  }
}
