package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {

    private String message;

    private Login data;

}
