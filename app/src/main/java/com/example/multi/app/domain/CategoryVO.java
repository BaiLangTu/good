package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class CategoryVO {

    private BigInteger id;

    private String name;

    private String image;
}
