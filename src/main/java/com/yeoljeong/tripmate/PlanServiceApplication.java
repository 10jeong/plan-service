package com.yeoljeong.tripmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.yeoljeong.tripmate.domain.model")
@EnableJpaRepositories("com.yeoljeong.tripmate")
public class PlanServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlanServiceApplication.class, args);
  }

}
