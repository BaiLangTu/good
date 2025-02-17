package com.example.multi.mapper;

import com.example.multi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;


@Mapper
public interface UserMapper {

    int userInsert(@Param("user") User user);

    // 根据帐号查询用户
    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findByPhone(@Param("phone") String phone);

    // 根据用户ID查询用户
    @Select("SELECT COUNT(1) FROM users WHERE user_id = #{userId}")
    User isUserExist(@Param("userId") BigInteger userId);


}
