package com.yeoljeong.tripmate.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record withdrawPlanUnitParticipationRequest(
    @NotBlank String reason
) {

}
