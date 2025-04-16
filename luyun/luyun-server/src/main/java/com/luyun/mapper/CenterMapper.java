package com.luyun.mapper;

import com.luyun.vo.CenterVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CenterMapper {
    @Select("select username, password, phone_number, email, role from users where user_id = #{userId}")
    public CenterVO getByUserId(long userId);

    @Update("update users set password = #{password} where user_id = #{userId}")
    public void changePassword(long userId, String password);

    @Update("update users set username = #{username} where user_id = #{userId}")
    public void updateUsername(long userId, String username);

    @Update("update users set phone_number = #{phoneNumber} where user_id = #{userId}")
    public void updatePhoneNumber(long userId, String phoneNumber);

    @Update("update users set email = #{email} where user_id = #{userId}")
    public void updateEmail(long userId, String email);

    @Insert("INSERT INTO users (username, password, phone_number, email, role) " +
            "VALUES (#{username}, #{password}, #{phoneNumber}, #{email}, #{role})")
    void createUser(@Param("username") String username,
                    @Param("password") String password,
                    @Param("phoneNumber") String phoneNumber,
                    @Param("email") String email,
                    @Param("role") int role);


    @Select("SELECT COUNT(1) FROM users WHERE phone_number = #{phoneNumber}")
    int countByPhoneNumber(String phoneNumber);

    @Select("SELECT COUNT(1) FROM users WHERE email = #{email}")
    int countByEmail(String email);
}
