package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Register {
    private String message;
}
