package com.example.multi.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class ConsoleListVo {
    private String massage;

    private Long total;

    private Integer pageSize;

    private List<ConsoleItemVo> items;

}
