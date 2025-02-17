package com.example.multi.console.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class ConsoleInfoVo {

    private List<String> goodsImages;

    private Integer price;

    private Integer sales;

    private String goodsName;

    private Integer sevenDayReturn;

    private String goodsDetails;

    private String createdTime;

    private String updatedTime;
}
