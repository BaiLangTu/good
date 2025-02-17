package com.example.multi.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class Goods {

    private BigInteger id;

    private BigInteger categoryId;

    private String title;

    private String goodsImages;

    private Integer sales;

    private String goodsName;

    private Integer price;

    private String source;

    private Integer sevenDayReturn;

    private String goodsDetails;

    private Integer createdTime;

    private Integer updatedTime;

    private Integer isDeleted;
}
