package com.luyun.service;
import com.luyun.dto.EmailRegisterDTO;
import com.luyun.dto.PhoneRegisterDTO;


public interface RegisterService {
    /**
     * 手机号注册
     */
    String phoneRegister(PhoneRegisterDTO phoneRegisterDTO);

    /**
     * 邮箱注册
     */
    String emailRegister(EmailRegisterDTO emailRegisterDTO);
}
