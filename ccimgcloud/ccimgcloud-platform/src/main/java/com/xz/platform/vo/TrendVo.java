package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class TrendVo {

    private Long albumId;

    private Long mid;

    private String albumName;

    private String imgsUrl;

    private String time;

    private String content;

    private Integer status;
}
