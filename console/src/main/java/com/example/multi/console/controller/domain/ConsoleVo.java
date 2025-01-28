package com.example.multi.console.controller.domain;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)

public class ConsoleVo {

    private String id;

    private String message;

}
