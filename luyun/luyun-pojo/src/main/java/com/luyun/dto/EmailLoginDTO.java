package com.luyun.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailLoginDTO implements Serializable {
    // 邮箱
    private String email;

    // 密码
    private String password;

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
}
