package com.luyun.controller;

import com.luyun.dto.ChangePasswordDTO;
import com.luyun.dto.CreateUserDTO;
import com.luyun.dto.UpdateInfoDTO;
import com.luyun.result.Result;
import com.luyun.service.CenterService;
import com.luyun.vo.CenterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080") // 跨域请求
@RestController
@RequestMapping("/center")
@Slf4j
public class CenterController {
    @Autowired
    private CenterService centerService;

    @GetMapping("/get")
    public Result<CenterVO> getInfo(@RequestParam long userId)
    {
        log.info("获取用户信息：{}", userId);
        CenterVO centerVO = centerService.getInfo(userId);
        return Result.success(centerVO);
    }

    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO)
    {
        log.info("修改密码：{}", changePasswordDTO);
        centerService.changePassword(changePasswordDTO);
        return Result.success("成功修改");
    }

    @PostMapping("/updateInfo")
    public Result<String> updateInfo(@RequestBody UpdateInfoDTO updateInfoDTO)
    {
        log.info("编辑信息：{}", updateInfoDTO);
        centerService.updateInfo(updateInfoDTO);
        return Result.success("成功编辑");
    }

    @PostMapping("/createUser")
    public Result<String> createUser(@RequestBody CreateUserDTO createUserDTO)
    {
        log.info("创建账户：{}", createUserDTO);
        centerService.createUser(createUserDTO);
        return Result.success("成功编辑");
    }

    @GetMapping("/existPhoneNumber")
    public Result<Boolean> existPhoneNumber(@RequestParam String phoneNumber)
    {
        log.info("数据库中是否已存在手机号：{}", phoneNumber);
        boolean res = centerService.existPhonNumber(phoneNumber);
        return Result.success(res);
    }

    @GetMapping("/existEmail")
    public Result<Boolean> existEmail(@RequestParam String email)
    {
        log.info("数据库中是否已存在邮箱：{}", email);
        boolean res = centerService.existEmail(email);
        return Result.success(res);
    }

}
