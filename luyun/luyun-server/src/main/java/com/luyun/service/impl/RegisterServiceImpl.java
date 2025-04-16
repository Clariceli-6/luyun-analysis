package com.luyun.service.impl;

import com.luyun.dto.EmailRegisterDTO;
import com.luyun.dto.PhoneRegisterDTO;
import com.luyun.mapper.RegisterMapper;
import com.luyun.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RegisterMapper registerMapper;

    @Override
    public String phoneRegister(PhoneRegisterDTO phoneRegisterDTO) {
        String username = phoneRegisterDTO.getUsername();
        String phoneNumber = phoneRegisterDTO.getPhoneNumber();
        String password = phoneRegisterDTO.getPassword();
        String passwordVerify = phoneRegisterDTO.getPasswordVerify();

        // md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        registerMapper.registerByPhone(username, password, phoneNumber);

        return "手机号注册成功";
    }

    @Override
    public String emailRegister(EmailRegisterDTO emailRegisterDTO) {
        String username = emailRegisterDTO.getUsername();
        String email = emailRegisterDTO.getEmail();
        String password = emailRegisterDTO.getPassword();
        String passwordVerify = emailRegisterDTO.getPasswordVerify();

        // md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        registerMapper.registerByEmail(username, password, email);

        return "邮箱注册成功";
    }
}
