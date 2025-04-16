package com.luyun.controller;
import com.luyun.dto.EmailRegisterDTO;
import com.luyun.dto.PhoneRegisterDTO;
import com.luyun.result.Result;
import com.luyun.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册功能
 */
@CrossOrigin(origins = "http://localhost:8080") // 跨域请求
@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    /**
     * 手机号注册
     */
    @PostMapping("/phone")
    public Result<String> phoneRegister(@RequestBody PhoneRegisterDTO phoneregisterDTO)
    {
        log.info("手机号注册：{}", phoneregisterDTO);
        String message = registerService.phoneRegister(phoneregisterDTO);
        return Result.success(message);
    }

    /**
     * 邮箱注册
     */
    @PostMapping("/email")
    public Result<String> emailRegister(@RequestBody EmailRegisterDTO emailRegisterDTO)
    {
        log.info("邮箱注册：{}", emailRegisterDTO);
        String message = registerService.emailRegister(emailRegisterDTO);
        return Result.success(message);
    }

}
