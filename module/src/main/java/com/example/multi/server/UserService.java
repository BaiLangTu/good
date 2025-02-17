package com.example.multi.server;

import com.example.multi.entity.User;
import com.example.multi.mapper.UserMapper;
import com.example.multi.utility.SignUtils;
import com.example.multi.utility.Utility;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletResponse response;

    public String register(String phone, String password, String name, String avatar) {


        // 检查用户是否已存在
        if (userExist(phone)) {
            return null;
        }
        // 工具类对象
        Utility utility = new Utility();

        // 生成盐
        String salt = utility.generateSalt();
        // MD5加盐加密
        String encryptPassword = utility.encryptToMd5(password,salt);


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
        userMapper.userInsert(user);
        SignUtils signUtils = new SignUtils();

        String sign = signUtils.generateSign(user.getId());

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
    public User isUserExist(BigInteger userId) {
        return userMapper.isUserExist(userId);
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
        SignUtils signUtils = new SignUtils();
        String sign = signUtils.generateSign(user.getId());
        return sign;

    }




    // 从 Sign 中提取 UserId
    public BigInteger getUserIdFromSign(String sign) {
        SignUtils signUtils = new SignUtils();
        return signUtils.getUserIdFromSign(sign);
    }

    // 校验 sign 是否有效
    public boolean validateSign(String sign,BigInteger expectedUserId) {
        SignUtils signUtils = new SignUtils();
        return signUtils.validateSign(sign,expectedUserId);
    }



}
