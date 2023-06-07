package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AgreeVo implements Serializable {


    private Long mid;

    private String cover;

    private Long uid;

    private String username;

    private String avatar;

    private String content;

    private Date createDate;

    private String time;

    // 0是评论，1是图片
    private Integer type;
}
