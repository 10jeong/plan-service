package com.yeoljeong.tripmate.presentation.external;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlanRedirectController {

  @GetMapping("/plans/create-page")
  public String planCreatePage() {
    return "forward:/plan_create.html";
  }

  @GetMapping("/plans/participate-page")
  public String planParticipatePage() {
    return "forward:/plan_participate.html";
  }

}
