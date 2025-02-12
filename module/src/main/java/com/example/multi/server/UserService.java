package com.example.multi.server;

import com.example.multi.entity.User;
import com.example.multi.mapper.UserMapper;
import com.example.multi.utility.AliOssUtility;
import com.example.multi.utility.Utility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public int register(String phone, String password, String name, MultipartFile avatar) {


        // 检查用户是否已存在
        if (userExist(phone)) {
            return 0;
        }
        // 注册上传图像
        AliOssUtility aliOssUtility = new AliOssUtility();
        String avatarUrl = aliOssUtility.uploadImage(avatar);

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
        user.setAvatar(avatarUrl);
        user.setUpdatedTime(timestamp);
        user.setIsDeleted(0);

        return userMapper.userInsert(user);
    }

    public Boolean userExist(String phone) {

        return userMapper.findByPhone(phone) != null;


    }

}
