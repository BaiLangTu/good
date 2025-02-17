package com.example.multi.mapper;

import com.example.multi.entity.Goods;
import com.example.multi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;


@Mapper
public interface UserMapper {

    // 根据帐号查询用户
    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findByPhone(@Param("phone") String phone);

    // 根据用户ID查询用户（未删除的用户）
    @Select("SELECT COUNT(1) FROM users WHERE user_id = #{userId} AND is_deleted = 0")
    User getById(@Param("userId") BigInteger userId);

   // 根据用户ID查询用户（所有用户，包括删除过的）
    @Select("select * from goods WHERE id=#{id}")
    Goods extractById(@Param("id") BigInteger id);

    // 新增用户
    int insert(@Param("user") User user);

    // 修改用户
    int update(@Param("user") User user);

    @Update("update user set is_deleted = 1,updated_time = #{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);




}
