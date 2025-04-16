package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateInfoDTO {
    @JsonProperty("user_id")
    private long userId;

    private String username;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;
}
