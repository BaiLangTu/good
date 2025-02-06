package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryGoodsVO {
    private List<CategoryVO> categories;  // 类目列表
    private GoodsVo goodsItem;    // 商品列表分页信息
}
