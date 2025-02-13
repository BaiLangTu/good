package com.example.multi.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class Sign {

    private BigInteger userId;

    private Integer expireTime;

    public Sign(BigInteger userId, Integer expireTime) {
        this.userId = userId;
        this.expireTime = expireTime;
    }
}
