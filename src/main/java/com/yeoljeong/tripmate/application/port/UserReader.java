package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import java.util.List;
import java.util.UUID;

public interface UserReader {

  List<UserData> getUser(List<UUID> userIds);
}
