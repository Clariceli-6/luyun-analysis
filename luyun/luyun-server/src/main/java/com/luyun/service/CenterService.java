package com.luyun.service;

import com.luyun.dto.ChangePasswordDTO;
import com.luyun.dto.CreateUserDTO;
import com.luyun.dto.UpdateInfoDTO;
import com.luyun.vo.CenterVO;

public interface CenterService {

    public CenterVO getInfo(long userId);

    public void changePassword(ChangePasswordDTO changePasswordDTO);

    void  updateInfo(UpdateInfoDTO updateInfoDTO);

    void createUser(CreateUserDTO createUserDTO);

    boolean existPhonNumber(String phoneNumber);

    boolean existEmail(String email);
}
