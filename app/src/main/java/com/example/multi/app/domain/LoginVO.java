package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {

    private String message;

    private LoginDataVO data;

    private String sign;

    public LoginVO(String message, LoginDataVO data, String sign) {
        this.message = message;
        this.data = data;
        this.sign = sign;
    }

}
