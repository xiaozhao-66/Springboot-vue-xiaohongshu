package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
public class AlbumVo implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     *
     */
    private String name;

    /**
     *
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

    /**
     * 收藏数量
     */
    private Long collectionCount;


    private Long uid;

    private String username;

    private String avatar;
}
