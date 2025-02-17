package com.example.multi.server;

import com.example.multi.VO.*;
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


    // 登录验证帐号和密码
    public LoginVO login(String phone, String password) {
        User user = userMapper.findByPhone(phone);

        // 如果用户不存在，返回失败的响应
        if (user == null) {
            return new LoginVO("用户不存在", null,null);
        }

        // 加密密码并验证
        Utility utility = new Utility();
        String salt = user.getSalt();
        String encryptPassword = utility.encryptToMd5(password, salt);

        // 如果密码错误，返回失败的响应
        if (!encryptPassword.equals(user.getPassword())) {
            return new LoginVO("密码错误", null,null);
        }

        // 登录成功，生成 sign
        SignUtils signUtils = new SignUtils();
        String sign = signUtils.generateSign(user.getId());

        LoginDataVO loginDataVO = new LoginDataVO();
        loginDataVO.setUserId(user.getId());
        loginDataVO.setPhone(user.getPhone());
        loginDataVO.setName(user.getName());
        loginDataVO.setAvatar(user.getAvatar());
        loginDataVO.setSign(sign);

        // 返回成功的响应，包含 sign
        return new LoginVO( "登录成功", loginDataVO,sign);
    }


    // 登录验证并处理 cookie 中的 sign
    public LoginPcVO loginToPc(String phone, String password) {
        User user = userMapper.findByPhone(phone);
        Utility utility = new Utility();
        SignUtils signUtils = new SignUtils();

        // 如果用户不存在，返回失败的响应
        if (user == null) {
            return new LoginPcVO("用户不存在", null);
        }


        // 加密密码并验证
        String salt = user.getSalt();
        String encryptedPassword = utility.encryptToMd5(password, salt);

        // 如果密码错误，返回失败的响应
        if (!encryptedPassword.equals(user.getPassword())) {
            return new LoginPcVO("密码错误", null);
        }

        // 登录成功，生成 sign
        String sign = signUtils.generateSign(user.getId());

        // 创建用户数据对象
        PcData data = new PcData();
        data.setUserId(user.getId());
        data.setPhone(phone);
        data.setName(user.getName());
        data.setAvatar(user.getAvatar());

        // 将 sign 存储到浏览器的 cookie 中
        Cookie cookie = new Cookie("sign", sign);  // 创建 sign 的 cookie
        cookie.setHttpOnly(true);  // 防止 JavaScript 访问
        cookie.setMaxAge(60 * 60);  // 设置 cookie 有效期为 1 小时
        cookie.setPath("/");  // 设置 cookie 的有效路径
        response.addCookie(cookie);  // 将 cookie 添加到响应中

        // 返回登录成功的响应
        return new LoginPcVO("登录成功", data);
    }


    // 从 Sign 中提取 UserId
    public BigInteger getUserIdFromSign(String sign) {
        SignUtils signUtils = new SignUtils();
        return signUtils.getUserIdFromSign(sign);
    }

    // 校验 sign 是否有效
    public boolean validateSign(String sign) {
        SignUtils signUtils = new SignUtils();
        return signUtils.validateSign(sign);
    }



}
