package com.example.multi.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class CategoryTree {

    private List<CategoryVO> data;
}
