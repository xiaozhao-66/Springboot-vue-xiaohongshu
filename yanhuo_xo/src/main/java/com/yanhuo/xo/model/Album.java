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
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_album")
public class Album extends BaseEntity {
    /**
     *专辑名称
     */
    private String name;
    /**
     * 专辑发布的用户id
     */
    private Long uid;
    /**
     * 专辑封面
     */
    private String cover;
    /**
     * 专辑排序
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