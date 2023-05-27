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
@TableName("t_comment")
public class CommentEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private Long mid;
    /**
     * 
     */
	private Long uid;
    /**
     * 
     */
	private Long rootId;
    /**
     * 
     */
	private Long replyId;
    /**
     * 
     */
	private Integer level;
    /**
     * 
     */
	private Integer sort;
    /**
     * 
     */
	private String content;
    /**
     * 
     */
	private Long creator;
    /**
     * 
     */
	private Date createDate;
}