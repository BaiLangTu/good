package com.example.multi.app.domain;

import com.example.multi.wrapper.ImageInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class GoodsItemVo  {

    private BigInteger id;

    private String categoryName;

    private String title;

    private ImageInfo goodsImage;

    private Integer sales;

    private Integer price;
}
