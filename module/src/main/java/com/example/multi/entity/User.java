package com.example.multi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    private Boolean id;

    private String phone;

    private String password;

    private String salt;

    private String name;

    private String avatar;

    private Integer createdTime;

    private Integer updatedTime;

    private Integer isDeleted;
}
