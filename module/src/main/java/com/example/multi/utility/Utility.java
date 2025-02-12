package com.example.multi.utility;

import com.alibaba.fastjson.JSON;
import com.example.multi.wrapper.ImageInfo;
import com.example.multi.wrapper.Wp;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {

    public String encodeWp(int page, int pageSize, String keyword) {
        Wp wp1 = new Wp();
        wp1.setPage(page);
        wp1.setPageSize(pageSize);
        wp1.setName(keyword);

        // JSON 序列化
        String jsonString = JSON.toJSONString(wp1);

        // Base64 编码
        String base64Encoded = Base64.getEncoder().encodeToString(jsonString.getBytes(StandardCharsets.UTF_8));
        String urlEncode = URLEncoder.encode(base64Encoded);
        return urlEncode;

//        // URL 编码
//        try {
//            return URLEncoder.encode(base64Encoded, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
    }

// ar的获取
    public ImageInfo getImageInfo(String imageUrl) {
        // 正则匹配宽和高
        Pattern pattern = Pattern.compile("_(\\d+)x(\\d+)");  // 假设宽高格式是 _宽x高
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));  // 宽度
            int height = Integer.parseInt(matcher.group(2));  // 高度
            double ar = (double) width / height;  // 计算AR值


            // 返回 ImageInfo 对象
            return new ImageInfo(imageUrl, ar);
        }

        // 如果没有匹配到宽高，返回默认值
        return new ImageInfo(imageUrl, 1.0);  // 默认值
    }

    // 生成盐
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);  // 返回盐的Base64编码字符串
    }

    // 将密码和盐一起进行MD5加密
    public String encryptToMd5(String password, String salt) {
        // 将密码和盐合并在一起
        String saltedPassword = password + salt;

        // 使用DigestUtils生成MD5加密
        return DigestUtils.md5Hex(saltedPassword);
    }


}
