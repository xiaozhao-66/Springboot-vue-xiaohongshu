package com.yanhuo.xo.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Data
@TableName("t_img_detail")
public class ImgDetail extends BaseEntity {

    /**
     * 图片信息内容
     */
    private String content;
    /**
     * 图片封面
     */
    private String cover;
    /**
     * 发布图片信息的用户id
     */
    private Long userId;
    /**
     * 图片所属的二级分类
     */
    private Long categoryId;
    /**
     * 图片所属的一级分类
     */
    private Long categoryPid;
    /**
     * 图片的地址信息
     */
    private String imgsUrl;
    /**
     * 图片数量
     */
    private Integer count;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图片的状态
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
     * 修改用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}