package com.luyun.controller;

import com.luyun.constant.JwtClaimsConstant;
import com.luyun.dto.EmailLoginDTO;
import com.luyun.entity.User;
import com.luyun.properties.JwtProperties;
import com.luyun.result.Result;
import com.luyun.service.LoginService;
import com.luyun.utils.JwtUtil;
import com.luyun.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.luyun.dto.PhoneLoginDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录功能
 */
@CrossOrigin(origins = "http://localhost:8080") // 跨域请求
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/phone")
    public Result<LoginVO> phonelogin(@RequestBody PhoneLoginDTO phoneLoginDTO)
    {
        log.info("手机号登陆：{}", phoneLoginDTO);

        User user =loginService.phoneLogin(phoneLoginDTO);

        if(user != null)
        {
            //登录成功后，生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
            claims.put(JwtClaimsConstant.ROLE, user.getRole());
            String token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    claims);

            LoginVO loginVO = LoginVO.builder()
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .token(token)
                    .build();

            return Result.success(loginVO);
        }
        else
        {
            return Result.error("登陆失败");
        }
    }

    @PostMapping("/email")
    public Result<LoginVO> emailLogin(@RequestBody EmailLoginDTO emailLoginDTO)
    {
        log.info("邮箱登录：{}", emailLoginDTO);

        User user =loginService.emailLogin(emailLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        claims.put(JwtClaimsConstant.ROLE, user.getRole());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        LoginVO loginVO = LoginVO.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();

        return Result.success(loginVO);

    }
}
