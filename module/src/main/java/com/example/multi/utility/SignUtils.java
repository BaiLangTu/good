package com.example.multi.utility;

import com.example.multi.wrapper.Sign;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;

public class SignUtils {

    private static final String SECRET_KEY = "your-secret-key";  // 可以在配置中管理
    private static final long EXPIRATION_TIME = 3600 * 1000;  // 1小时过期时间

    // 生成 sign
    public String generateSign(BigInteger userId) {
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

    // 验证 sign 是否有效
    public  boolean validateSign(String sign) {
        try {
            // 解码 sign
            byte[] decodedBytes = Base64.getDecoder().decode(sign);
            String decodedString = new String(decodedBytes);
            Sign signObject = new ObjectMapper().readValue(decodedString, Sign.class);

            // 验证过期时间
            return signObject.getExpireTime() > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }

    // 获取 sign 中的 userId
    public BigInteger getUserIdFromSign(String sign) {
        try {
            // 解码 sign
            byte[] decodedBytes = Base64.getDecoder().decode(sign);
            String decodedString = new String(decodedBytes);
            Sign signObject = new ObjectMapper().readValue(decodedString, Sign.class);

            return signObject.getUserId();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting userId from sign", e);
        }
    }



    // HMAC-SHA256 加密
    private  String encryptWithHmacSHA256(String data) {
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
