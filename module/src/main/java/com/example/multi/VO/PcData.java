package com.example.multi.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class PcData {
    private BigInteger userId;

    private String phone;

    private String password;

    private String name;

    private String avatar;

    private String sign;
}
