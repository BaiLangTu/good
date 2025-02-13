package com.example.multi.console.controller.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Login {

    private String phone;

    private String password;

    private String name;

    private String avatar;

}
