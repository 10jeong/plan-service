package com.yeoljeong.tripmate.infrastructer.external.user;

import java.util.List;
import java.util.UUID;

public record GetUserRequest(
    List<UUID> userIds
) {

  public static GetUserRequest from(List<UUID> userIds) {
    return new GetUserRequest(userIds);
  }
}
