package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;

    private String password;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private int role;
}
