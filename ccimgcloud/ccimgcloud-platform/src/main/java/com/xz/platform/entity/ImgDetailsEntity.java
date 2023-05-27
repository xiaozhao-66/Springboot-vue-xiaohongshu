package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Data
@TableName("t_img_details")
public class ImgDetailsEntity extends BaseEntity {

    /**
     *
     */
    private String content;
    /**
     *
     */
    private String cover;
    /**
     *
     */
    private Long userId;
    /**
     *
     */
    private Long categoryId;
    /**
     *
     */
    private Long categoryPid;
    /**
     *
     */
    private String imgsUrl;
    /**
     * 图片数量
     */
    private Integer count;
    /**
     *
     */
    private Integer sort;
    /**
     *
     */
    private Integer status;

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


    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}