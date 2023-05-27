package com.xz.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class CategoryEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private String name;
    /**
     * 
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
     * 
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
     * 
     */
	private Long creator;
    /**
     * 
     */
	private Date createDate;
    /**
     * 
     */
	private Long updater;
    /**
     * 
     */
	private Date updateDate;
}