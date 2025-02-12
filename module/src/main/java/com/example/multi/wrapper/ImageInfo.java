package com.example.multi.wrapper;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageInfo {

    private String url;
    private double ar;

    // 构造函数
    public ImageInfo(String url, double ar) {
        this.url = url;
        this.ar = ar;
    }

}
