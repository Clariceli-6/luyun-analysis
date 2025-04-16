package com.luyun.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import static com.luyun.enumeration.OperationType.INSERT;

@Mapper
public interface RegisterMapper {
    /**
     * 根据手机号注册
     */
    @Insert("INSERT INTO users (username, password, phone_number, email, role) VALUES (#{username}, #{password}, #{phoneNumber}, null, 0)")
    void registerByPhone(@Param("username") String username,
                         @Param("password") String password,
                         @Param("phoneNumber") String phoneNumber);

    /**
     * 根据邮箱注册
     */
    @Insert("INSERT INTO users (username, password, phone_number, email, role) VALUES (#{username}, #{password}, null, #{email}, 0)")
    void registerByEmail(@Param("username") String username,
                         @Param("password") String password,
                         @Param("email") String email);
}
