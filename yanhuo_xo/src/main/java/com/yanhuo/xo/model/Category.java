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
@TableName("t_category")
public class Category extends BaseEntity {

    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类的父级id
     */
    private Long pid;

    /**
     * 描述
     */
    private String description;

    /**
     * 热门的分类
     */
    private Long count;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 随便看看封面
     */
    private String cover;
    /**
     * 热门封面
     */
    private String hotCover;

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