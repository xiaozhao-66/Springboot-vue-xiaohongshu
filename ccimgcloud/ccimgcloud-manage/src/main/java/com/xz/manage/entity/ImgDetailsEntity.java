package com.xz.manage.entity;

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
@TableName("t_img_details")
public class ImgDetailsEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private String title;
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
	private String imgsUrl;
    /**
     * 
     */
	private Integer sort;
    /**
     * 
     */
	private Integer status;
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