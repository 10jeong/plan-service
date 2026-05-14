package com.yeoljeong.tripmate.infrastructer.external.user;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import com.yeoljeong.tripmate.application.port.UserReader;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

  private final UserFeignClient userFeignClient;

  @Override
  public List<UserData> getUser(GetUserRequest userIds) {
    return userFeignClient.getUser(userIds);
  }

}
