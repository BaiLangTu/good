package com.example.multi.app.controller;

import com.example.multi.app.domain.Login;
import com.example.multi.app.domain.LoginVO;
import com.example.multi.app.domain.Register;
import com.example.multi.entity.User;
import com.example.multi.server.UserService;
import com.example.multi.utility.SignUtils;
import com.example.multi.wrapper.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("goods/register")
    public Register register(@RequestParam(name = "phone") String phone,
                             @RequestParam(name = "password") String password,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "avatar") MultipartFile avatar,
                             @RequestParam(required = false) String sign) {
        Register register = new Register();



        // 判断是否已登录
        if (sign != null && userService.validateSign(sign)) {
            register.setMessage("已登录，不能进行注册");
            return register;
        }

        // 用户是否存在
        if (userService.userExist(phone)) {
            register.setMessage("该用户帐号已注册过");
            return register;
        }

        int result = userService.register(phone, password, name, avatar);
        if (result == 1) {
            register.setMessage("注册成功");

            // 4. 注册成功后，生成 sign
            String userSign = userService.login(phone, password);
            // 返回 sign 给 app
            register.setSign(userSign);
        } else {
            register.setMessage("注册失败");
        }
        return register;
    }

    @RequestMapping("goods/login")
    public LoginVO login(@RequestParam(name = "phone") String phone,
                         @RequestParam(name = "password") String password) {
        // 验证用户信息
        String loginResult = userService.login(phone, password);

        LoginVO loginResponse = new LoginVO();

        if (loginResult.equals("用户不存在") || loginResult.equals("密码错误")) {
            loginResponse.setMessage("用户名或密码错误");
        } else {
            Login login = new Login();
            loginResponse.setMessage("登录成功");
            User user = userService.getUserByPhone(phone);
            login.setPhone(user.getPhone());
            login.setPassword(user.getPassword());
            login.setName(user.getName());
            login.setAvatar(user.getAvatar());
            SignUtils signUtils = new SignUtils();
            String sign = signUtils.generateSign(user.getId());
            login.setSign(sign);
            loginResponse.setData(login);

        }
        return loginResponse;
    }
}
