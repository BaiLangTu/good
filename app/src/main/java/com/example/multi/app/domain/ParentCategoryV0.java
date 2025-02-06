package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class ParentCategoryV0 {

    private BigInteger id;

    private String name;

    private String image;

    private List<CategoryItemVo> subCategories;


}
