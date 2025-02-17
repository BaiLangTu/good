package com.example.multi.app.controller;

import com.example.multi.app.domain.LoginDataVO;
import com.example.multi.app.domain.LoginVO;
import com.example.multi.app.domain.RegisterVO;
import com.example.multi.entity.User;
import com.example.multi.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.math.BigInteger;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("user/register")
    public RegisterVO register(@RequestParam(name = "phone") String phone,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "avatar") String avatar,
                               @RequestParam(required = false) String sign) {
        RegisterVO registerVO = new RegisterVO();

        BigInteger userIdFromSign = userService.getUserIdFromSign(sign);

        // 判断是否已登录
        if (sign != null && userService.validateSign(sign,userIdFromSign)) {

            registerVO.setMessage("已登录，不能进行注册");
            return registerVO;
        }

        // 用户是否存在
        if (userService.userExist(phone)) {
            registerVO.setMessage("该用户帐号已注册过");
            return registerVO;
        }

        String register = userService.register(phone, password, name, avatar);
        if (register == null) {
            registerVO.setMessage("注册失败");

        } else {
            registerVO.setMessage("注册成功");
            // 4. 注册成功后，生成 sign
            // 返回 sign 给 app
            registerVO.setSign(register);

            //  如果是浏览器端，将 sign 保存到 cookie
            // 登录成功，设置cookie
            Cookie cookie = new Cookie("sign", register);  // 创建sign的cookie
            cookie.setHttpOnly(true);  // 防止JavaScript访问
            cookie.setMaxAge(60 * 60);  // 设置cookie有效期为1小时
            response.addCookie(cookie);  // 将cookie添加到响应
        }
        return registerVO;
    }

    @RequestMapping("user/login")
    public LoginVO login(@RequestParam(name = "phone") String phone,
                         @RequestParam(name = "password") String password) {

        String sign;
        User user = null;
        try {
            sign = userService.login(phone, password);  // 调用 userService 进行登录验证
        } catch (RuntimeException e) {
            return new LoginVO("登录失败: " + e.getMessage(), null, null);  // 登录失败返回错误信息
        }

        try {
            // 获取用户信息
            user = userService.getUserByPhone(phone);  // 获取用户信息
        } catch (RuntimeException e) {
            return new LoginVO("获取用户信息失败: " + e.getMessage(), null, null);  // 登录失败返回错误信息

        }
        LoginDataVO loginDataVO = new LoginDataVO();
        loginDataVO.setUserId(user.getId());
        loginDataVO.setSign(sign);
        loginDataVO.setName(user.getName());
        loginDataVO.setPhone(user.getPhone());
        loginDataVO.setAvatar(user.getAvatar());

        LoginVO loginVO = new LoginVO("登录成功", loginDataVO, sign);
        return loginVO;
    }
}
