package com.xz.manage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_tag")
public class TagEntity {

    /**
     * 
     */
	private Long id;
    /**
     *
     */
    private Long uid;
    /**
     * 
     */
	private String name;
    /**
     * 
     */
	private Integer sort;
    /**
     * 
     */
	private Long creator;
    /**
     * 
     */
	private Date createDate;
}