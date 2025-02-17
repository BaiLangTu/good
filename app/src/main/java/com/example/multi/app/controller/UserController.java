package com.example.multi.app.controller;

import com.example.multi.VO.LoginVO;
import com.example.multi.app.domain.RegisterVO;
import com.example.multi.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;


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



        // 判断是否已登录
        if (sign != null && userService.validateSign(sign)) {
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
        return userService.login(phone, password);
    }
}
