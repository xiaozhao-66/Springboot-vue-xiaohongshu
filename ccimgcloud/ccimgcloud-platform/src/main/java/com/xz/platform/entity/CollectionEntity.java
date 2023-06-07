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
@TableName("t_collection")
public class CollectionEntity extends BaseEntity {

    /**
     * 收藏的用户id
     */
    private Long uid;
    /**
     * 收藏类型的id
     */
    private Long collectionId;

    //收藏的类型（0是图片，1是专辑）
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;
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