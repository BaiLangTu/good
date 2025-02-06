package com.example.multi.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class Wp implements Serializable {
    public Integer page;
    public Integer pageSize;
    public BigInteger categoryId;
    public String name;

}
