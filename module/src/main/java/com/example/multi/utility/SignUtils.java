package com.example.multi.utility;

import com.example.multi.wrapper.Sign;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;

 public class SignUtils {

    private static final String SECRET_KEY = "xiaobai115";
    private static final long EXPIRATION_TIME = 3600 * 1000;  // 1小时过期时间

    // 生成 sign
    public static String generateSign(BigInteger userId) {
        int expireTime = (int) (System.currentTimeMillis()*1000 + EXPIRATION_TIME);  // 1小时过期时间
        Sign signObject = new Sign(userId,expireTime);

        try {
            // 将 Sign 对象转换为 JSON 字符串
            String json = new ObjectMapper().writeValueAsString(signObject);

            // Base64 编码
            String base64Encoded = Base64.getEncoder().encodeToString(json.getBytes());

            return encryptWithHmacSHA256(base64Encoded);
        } catch (Exception e) {
            throw new RuntimeException("Error generating sign", e);
        }
    }


     // HMAC-SHA256 加密
    private static String encryptWithHmacSHA256(String data) {
        try {
            // 使用 SECRET_KEY 进行加密
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting sign", e);
        }
    }
}
