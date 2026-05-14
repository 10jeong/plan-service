package com.yeoljeong.tripmate.application.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UserData(
    @JsonProperty("id")
    UUID userId,
    String email,
    @JsonProperty("name")
    String userName
) {

}
