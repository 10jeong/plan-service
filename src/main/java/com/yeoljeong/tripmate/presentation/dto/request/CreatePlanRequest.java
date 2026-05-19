package com.yeoljeong.tripmate.presentation.dto.request;

import com.yeoljeong.tripmate.application.dto.command.CreatePlanCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public record CreatePlanRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotBlank String planType,
    @NotNull MultipartFile image,
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
          .image(image)
          .planUnit(
              planUnits.stream()
                  .map(CreatePlanUnitRequest::toCommand)
                  .toList()
          )
          .build();
    }

}
