package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.application.dto.command.CreatePlanCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreatePlanRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotBlank String planType,
    @NotEmpty List<@Valid CreatePlanUnitRequest> planUnits
) {
    public CreatePlanCommand toCommand(UUID hostId) {
      return CreatePlanCommand.builder()
          .hostId(hostId)
          .title(title)
          .description(description)
          .startDate(startDate)
          .endDate(endDate)
          .planType(planType)
          .planUnit(
              planUnits.stream()
                  .map(CreatePlanUnitRequest::toCommand)
                  .toList()
          )
          .build();
    }

}
