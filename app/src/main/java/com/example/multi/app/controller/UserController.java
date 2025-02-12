package com.example.multi.app.controller;

import com.example.multi.app.domain.Register;
import com.example.multi.entity.User;
import com.example.multi.server.UserService;
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
                             @RequestParam(name = "avatar") MultipartFile avatar) {
        Register register = new Register();
        // 用户是否存在
        if (userService.userExist(phone)) {
            register.setMessage("该用户帐号已注册过");
            return register;
        }


        int result = userService.register(phone, password, name, avatar);
        if (result == 1) {
            register.setMessage("注册成功");
        } else {
            register.setMessage("注册失败");
        }
        return register;
    }
}
