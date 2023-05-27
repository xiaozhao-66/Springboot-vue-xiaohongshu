package com.xz.manage.entity;

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
 * @since 1.0.0 2023-03-13
 */
@Data
@TableName("t_category")
public class CategoryEntity extends BaseEntity {


    /**
     * 
     */
	private String name;
    /**
     * 
     */
	private Long pid;
    /**
     * 
     */
	private Integer sort;

	private String cover;

	private String hotCover;

	private String description;
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