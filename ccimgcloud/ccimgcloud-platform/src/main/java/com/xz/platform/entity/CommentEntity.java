package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_comment")
public class CommentEntity extends BaseEntity {


    /**
     *
     */
    private Long mid;
    /**
     *
     */
    private Long uid;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String avatar;
    /**
     *
     */
    private Long pid;
    /**
     *
     */
    private Long replyId;
    /**
     *
     */
    private String replyName;
    /**
     *
     */
    private Integer level;
    /**
     *
     */
    private Integer sort;
    /**
     *
     */
    private String content;

    /**
     * 点赞次数
     */
    private Long count;
}