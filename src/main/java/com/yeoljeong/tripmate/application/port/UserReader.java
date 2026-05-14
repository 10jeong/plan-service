package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.application.dto.external.UserData;
import com.yeoljeong.tripmate.infrastructer.external.user.GetUserRequest;
import java.util.List;

public interface UserReader {

  List<UserData> getUser(GetUserRequest userIds);
}
