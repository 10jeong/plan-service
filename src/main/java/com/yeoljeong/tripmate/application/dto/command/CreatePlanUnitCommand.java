package com.yeoljeong.tripmate.application.dto.command;


import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreatePlanUnitCommand {
  private int day;
  private int orderIndex;
  private String title;
  private String description;
  private LocalTime startTime;
  private LocalTime endTime;
  private BigDecimal price;
  private int maxCount;
  private UUID productScheduleId;

}
