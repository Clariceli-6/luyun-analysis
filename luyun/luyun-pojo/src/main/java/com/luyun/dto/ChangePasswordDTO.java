package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @JsonProperty("user_id")
    private long userId;

    private String password;
}
