package com.yeoljeong.tripmate.application.dto.command;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class CreatePlanCommand {
  private UUID hostId;
  private String title;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private String planType;
  private MultipartFile image;
  private List<CreatePlanUnitCommand> planUnit;
}
