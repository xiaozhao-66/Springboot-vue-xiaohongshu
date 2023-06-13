package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

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
     * 评论的图片信息id
     */
    private Long mid;
    /**
     * 发布评论的用户id
     */
    private Long uid;

    /**
     * 发布评论的用户名
     */
    private String username;

    /**
     * 发布评论的用户头像
     */
    private String avatar;
    /**
     * 评论的父id
     */
    private Long pid;
    /**
     * 回复某一条评论的id
     */
    private Long replyId;
    /**
     * 回复某一条评论的用户
     */
    private String replyName;
    /**
     * 评论等级
     */
    private Integer level;
    /**
     * 评论排序
     */
    private Integer sort;
    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞次数
     */
    private Long count;

    private Long twoNums;
}