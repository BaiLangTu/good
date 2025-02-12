package com.example.multi.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Wp implements Serializable {

    public Integer page;
    public Integer pageSize;
    public String name;

}
