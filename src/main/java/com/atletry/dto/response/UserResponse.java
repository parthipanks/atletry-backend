package com.atletry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "User profile")
public class UserResponse {
    private Long          id;
    private String        mobile;
    private String        name;
    private String        city;
    private String        profileImageUrl;
    private Double        latitude;
    private Double        longitude;
    private boolean       profileComplete;
    private List<String>  roles;
    private ZonedDateTime createdDate;
    private List<UserSportResponse> sports;
}
