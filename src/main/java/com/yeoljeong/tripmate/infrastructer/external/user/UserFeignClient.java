package com.yeoljeong.tripmate.infrastructer.external.user;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserFeignClient {

  @PostMapping("/internal/users")
  List<UserData> getUser(@RequestBody GetUserRequest userIds);
}
