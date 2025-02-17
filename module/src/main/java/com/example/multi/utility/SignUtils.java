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

    // 验证 sign 是否有效
    public static boolean validateSign(String sign,BigInteger expectedUserId) {
        try {
            // 解码 sign
            byte[] decodedBytes = Base64.getDecoder().decode(sign);
            String decodedString = new String(decodedBytes);
            Sign signObject = new ObjectMapper().readValue(decodedString, Sign.class);
            // 校验过期时间
            if (signObject.getExpireTime() < System.currentTimeMillis() * 1000) {
                return false;  // 如果过期了，返回 false
            }

            // 校验用户 ID
            if (!signObject.getUserId().equals(expectedUserId)) {
                return false;  // 如果用户 ID 不匹配，返回 false
            }

            return true;  // sign 验证通过

        } catch (Exception e) {
            return false;
        }
    }

    // 获取 sign 中的 userId
    public static BigInteger getUserIdFromSign(String sign) {
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
