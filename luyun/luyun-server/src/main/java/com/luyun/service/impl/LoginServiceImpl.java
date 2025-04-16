package com.luyun.service.impl;

import com.luyun.constant.MessageConstant;
import com.luyun.dto.EmailLoginDTO;
import com.luyun.dto.PhoneLoginDTO;
import com.luyun.entity.User;
import com.luyun.exception.AccountNotFoundException;
import com.luyun.exception.PasswordErrorException;
import com.luyun.mapper.LoginMapper;
import com.luyun.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    /**
     * 手机号登录
     * @param phoneLoginDTO
     * @return
     */
    @Override
    public User phoneLogin(PhoneLoginDTO phoneLoginDTO) {
        String phoneNumber = phoneLoginDTO.getPhoneNumber();
        String password = phoneLoginDTO.getPassword();

        // 1. 根据用户名查询数据库中的数据
        User user =loginMapper.getByPhone(phoneNumber);

        // 2. 处理各种异常情况
        if(user == null){
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return user;
    }

    @Override
    public User emailLogin(EmailLoginDTO emailLoginDTO) {
        String email = emailLoginDTO.getEmail();
        String password = emailLoginDTO.getPassword();

        // 1. 根据用户名查询数据库中的数据
        User user =loginMapper.getByEmail(email);

        // 2. 处理各种异常情况
        if(user == null){
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return user;
    }
}
