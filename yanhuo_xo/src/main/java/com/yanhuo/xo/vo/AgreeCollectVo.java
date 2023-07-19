package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AgreeCollectVo implements Serializable {

    private Long aid;

    private Long mid;

    private String cover;

    private Long uid;

    private String username;

    private String avatar;

    private String content;

    private String name;

    private Integer count;

    /**
     * 图片数量
     */
    private Long imgCount;

    /**
     * 收藏数量
     */
    private Long collectionCount;

    // 0是评论，1是图片,2专辑
    private Integer type;

    private Date createDate;

}
