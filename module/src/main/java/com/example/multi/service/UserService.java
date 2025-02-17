package com.example.multi.service;

import com.example.multi.entity.User;
import com.example.multi.mapper.UserMapper;
import com.example.multi.utility.SignUtils;
import com.example.multi.utility.Utility;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletResponse response;

    public String insert(String phone, String password, String name, String avatar) {


        // 检查用户是否已存在
        if (userExist(phone)) {
            throw new RuntimeException("用户已注册");
        }

        // 生成盐
        String salt = Utility.generateSalt();
        // MD5加盐加密
        String encryptPassword = Utility.encryptToMd5(password,salt);


        int timestamp = (int) (System.currentTimeMillis() / 1000);
        User user = new User();
        user.setPhone(phone);
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        user.setName(name);
        user.setCreatedTime(timestamp);
        user.setAvatar(avatar);
        user.setUpdatedTime(timestamp);
        user.setIsDeleted(0);
        userMapper.insert(user);

        String sign = SignUtils.generateSign(user.getId());

        // 注册成功后，立即生成 sign
        return sign; // 直接返回 sign
    }

    public Boolean userExist(String phone) {

        return userMapper.findByPhone(phone) != null;

    }

    // 根据手机号获取用户信息
    public User getUserByPhone(String phone) {
        return userMapper.findByPhone(phone);

    }


    // 检查用户是否存在
    public User getById(BigInteger userId) {
        return userMapper.getById(userId);
    }

    public String login(String phone, String password) {

        User user = userMapper.findByPhone(phone);
        // 加密密码并验证
        Utility utility = new Utility();
        String salt = user.getSalt();
        String encryptPassword = utility.encryptToMd5(password, salt);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 如果密码错误，返回失败的响应
        if (!encryptPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 登录成功，生成 sign
        String sign = SignUtils.generateSign(user.getId());
        return sign;

    }




    // 从 Sign 中提取 UserId
    public BigInteger getUserIdFromSign(String sign) {
        return SignUtils.getUserIdFromSign(sign);
    }

    // 校验 sign 是否有效
    public boolean validateSign(String sign,BigInteger expectedUserId) {
        return SignUtils.validateSign(sign,expectedUserId);
    }

    // 删除商品
    public int deleteGoods(BigInteger id){ return userMapper.delete(id,(int)(System.currentTimeMillis() / 1000)); }



}
