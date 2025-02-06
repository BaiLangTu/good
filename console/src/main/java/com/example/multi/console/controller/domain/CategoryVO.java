package com.example.multi.console.controller.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryVO {

    private BigInteger id;

    private String name;

    private List<CategoryVO> children = new ArrayList<>();

}
