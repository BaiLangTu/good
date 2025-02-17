package com.example.multi.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginDataVO {

    private String phone;

    private String password;

    private String name;

    private String avatar;

}
