package com.yeoljeong.tripmate.application.service.command;

import com.yeoljeong.tripmate.application.dto.command.FindParticipantStatusCommand;
import com.yeoljeong.tripmate.application.dto.result.FindParticipationStatusResult;
import com.yeoljeong.tripmate.domain.model.plan.PlanParticipation;
import com.yeoljeong.tripmate.domain.repository.PlanParticipationRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanInternalCommandService {

  private final PlanParticipationRepository planParticipationRepository;

  public FindParticipationStatusResult findParticipationStatusByPlanUnitIdAndUserId(UUID planUnitId, UUID userId) {

    PlanParticipation participation = planParticipationRepository.findByPlanUnit_IdAndUserId(planUnitId, userId);

    return new FindParticipationStatusResult(planUnitId, userId,
        participation.getParticipationStatus());
  }

}
