package com.yeoljeong.tripmate.application.dto.command;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreatePlanCommand {
  private UUID hostId;
  private String title;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String planType;
  private List<CreatePlanUnitCommand> planUnit;
}
