package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_album")
public class AlbumEntity extends BaseEntity {


    /**
     *
     */
    private String name;
    /**
     *
     */
    private Long uid;
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