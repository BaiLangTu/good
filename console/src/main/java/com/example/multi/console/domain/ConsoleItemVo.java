package com.example.multi.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ConsoleItemVo  {

    private BigInteger id;

    private String title;

    private String goodImage;

    private Integer sales;

    private Integer price;


}
