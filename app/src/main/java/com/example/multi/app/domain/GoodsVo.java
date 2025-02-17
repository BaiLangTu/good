package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsVo {

    private Boolean isEnd;

    private String wP;

    private List<GoodsItemVo> list;


}
