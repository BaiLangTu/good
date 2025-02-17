package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)

public class GoodsInfoVo  {

    private String categoryName;

    private String categoryImage;

    private List<String> goodsImages;

    private String source;

    private Integer price;

    private Integer sales;

    private String goodsName;

    private Integer sevenDayReturn;

    private String goodsDetails;
}
