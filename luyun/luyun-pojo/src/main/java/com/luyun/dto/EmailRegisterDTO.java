package com.luyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailRegisterDTO {
    // 用户名 主键
    private String username;

    // 手机号
    private String email;

    // 密码
    private String password;

    // 确认密码
    @JsonProperty("password_verify")
    private String passwordVerify;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String password_verify) {
        this.passwordVerify = password_verify;
    }
}
