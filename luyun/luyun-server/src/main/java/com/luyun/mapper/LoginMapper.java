package com.luyun.mapper;

import com.luyun.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select * from users where phone_number = #{phoneNumber}")
    User getByPhone(String phoneNumber);

    @Select("select * from users where email = #{email}")
    User getByEmail(String email);
}
