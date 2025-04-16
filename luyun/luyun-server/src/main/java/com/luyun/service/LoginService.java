package com.luyun.service;

import com.luyun.dto.EmailLoginDTO;
import com.luyun.dto.PhoneLoginDTO;
import com.luyun.entity.User;

public interface LoginService {
    /**
     * 手机登录
     */
    User phoneLogin(PhoneLoginDTO phoneLoginDTO);

    /**
     * 邮箱登录
     */
    User emailLogin(EmailLoginDTO emailLoginDTO);
}
