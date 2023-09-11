package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class TrendVo {

    private Long mid;

    private Long userId;

    private String username;

    private String avatar;

    private String content;

    private String imgsUrl;

    private Long albumId;

    private String albumName;

    private Long agreeCount;

    private Long commentCount;

    private Boolean isAgree;

    private Integer status;

    private Date time;
}
