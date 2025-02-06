package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryLevel3DetailsVO {

    private BigInteger id;

    private BigInteger parentId;

    private String name;

    private String image;

    private List<GoodsVo> goodsVosList;

}
