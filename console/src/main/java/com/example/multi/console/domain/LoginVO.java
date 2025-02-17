package com.example.multi.app.domain;

import com.example.multi.console.domain.LoginDataVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {

    private String message;

    private LoginDataVO data;



    public LoginVO(String message, LoginDataVO data, String sign) {
        this.message = message;
        this.data = data;

    }

}
