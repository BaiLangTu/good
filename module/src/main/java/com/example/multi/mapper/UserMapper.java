package com.example.multi.mapper;

import com.example.multi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    int userInsert(@Param("user") User user);

    // 根据帐号查询用户
    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findByPhone(@Param("phone") String phone);
}
