package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录返回的数据格式
 */
@Data
@Builder
public class LoginVO implements Serializable {
    // 用户名
    private String username;

    // 手机号与邮箱有且只有一个
    // 手机号
    @JsonProperty("phone_number")
    private String phoneNumber;

    // 邮箱
    private String email;

    // 角色
    private Integer role; // 0用户，1管理员

    // 令牌
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
