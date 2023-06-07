package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class CollectionVo implements Serializable {

    /**
     *
     */
    private Long id;

    private Long collectionId;
    /**
     *专辑或图片名称
     */

    private String content;

    /**
     *专辑或图片封面
     */
    private String cover;
    /**
     *
     */
    private Integer sort;

    /**
     * 图片数量
     */
    private Long imgCount;

    private Integer count;

    /**
     * 收藏数量
     */
    private Long collectionCount;

    private Long agreeCount;

    private Long uid;

    private String username;

    private String avatar;



    private Date collectionTime;
}
