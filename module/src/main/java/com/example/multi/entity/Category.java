package com.example.multi.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain = true)
public class Category {

    // 类目id
    private BigInteger id;
    // 父类目id
    private BigInteger parentId;
    //类目名称
    private String name;
    //类目图片
    private String image;
    //创建时间
    private Integer createdTime;
    //更新时间
    private Integer updatedTime;
    //删除
    private Integer isDeleted;
}