package com.yanhuo.xo.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImgDetailState implements Serializable {

    private Long mid;

    /**
     * 点赞数量
     */
    private Long agreeCount;

    /**
     * 收藏数量
     */
    private Long collectionCount;

    /**
     * 评论数量
     */
    private Long commentCount;

    /**
     * 浏览数量
     */
    private Long viewCount;
}
