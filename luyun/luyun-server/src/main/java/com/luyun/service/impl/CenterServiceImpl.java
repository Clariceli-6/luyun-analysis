package com.luyun.service.impl;

import com.luyun.dto.ChangePasswordDTO;
import com.luyun.dto.CreateUserDTO;
import com.luyun.dto.UpdateInfoDTO;
import com.luyun.mapper.CenterMapper;
import com.luyun.service.CenterService;
import com.luyun.vo.CenterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class CenterServiceImpl implements CenterService {
    @Autowired
    private CenterMapper centerMapper;

    @Override
    public CenterVO getInfo(long userId) {
        CenterVO centerVO = centerMapper.getByUserId(userId);
        return centerVO;
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        long userId = changePasswordDTO.getUserId();
        String password = changePasswordDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        centerMapper.changePassword(userId, password);
        return;
    }

    @Override
    public void updateInfo(UpdateInfoDTO updateInfoDTO) {
        long userId = updateInfoDTO.getUserId();
        String username = updateInfoDTO.getUsername();
        String phoneNumber = updateInfoDTO.getPhoneNumber();
        String email = updateInfoDTO.getEmail();

        if(username != "" && username != null){
            centerMapper.updateUsername(userId, username);
        }
        if(phoneNumber != "" && phoneNumber != null){
            centerMapper.updatePhoneNumber(userId, phoneNumber);
        }
        if(email != "" && email != null){
            centerMapper.updateEmail(userId, email);
        }

        return;
    }

    @Override
    public void createUser(CreateUserDTO createUserDTO) {
        String username = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        String phoneNumber = createUserDTO.getPhoneNumber();
        String email = createUserDTO.getEmail();
        int role = createUserDTO.getRole();

        centerMapper.createUser(username, password, phoneNumber, email, role);
        return;
    }

    @Override
    public boolean existPhonNumber(String phoneNumber) {
        int count =  centerMapper.countByPhoneNumber(phoneNumber);
        if(count > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean existEmail(String email) {
        int count =  centerMapper.countByEmail(email);
        if(count > 0)
            return true;
        else
            return false;
    }
}
