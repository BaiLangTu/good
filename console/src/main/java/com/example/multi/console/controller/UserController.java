package com.example.multi.console.controller;


import com.example.multi.console.domain.ConsoleVo;
import com.example.multi.console.domain.LoginDataVO;
import com.example.multi.console.domain.LoginVO;
import com.example.multi.entity.User;
import com.example.multi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/user/login")
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
        loginDataVO.setName(user.getName());
        loginDataVO.setPhone(user.getPhone());
        loginDataVO.setAvatar(user.getAvatar());

        // 将 sign 存储到浏览器的 cookie 中
        Cookie cookie = new Cookie("sign", sign);  // 创建 sign 的 cookie
        cookie.setHttpOnly(true);  // 防止 JavaScript 访问
        cookie.setMaxAge(60 * 60);  // 设置 cookie 有效期为 1 小时
        cookie.setPath("/");  // 设置 cookie 的有效路径
        response.addCookie(cookie);  // 将 cookie 添加到响应中

        LoginVO loginVO = new LoginVO("登录成功", loginDataVO, sign);
        return loginVO;

    }


    @RequestMapping("/user/delete")
    public ConsoleVo userDelete (@RequestParam(name = "userId") BigInteger userId) {
        int result = userService.delete(userId);
        ConsoleVo consoleVo = new ConsoleVo();
        if( result == 1){
            consoleVo.setMessage("用户删除成功");
        } else {
            consoleVo.setMessage("用户删除失败");
        }
        return consoleVo;

    }



}
