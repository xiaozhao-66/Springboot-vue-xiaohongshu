package com.xz.platform.entity;

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
@TableName("t_message")
public class MessageEntity extends BaseEntity {


	private Long sendId;
	/**
	 *
	 */
	private Long acceptId;
	/**
	 *
	 */
	private String content;
	/**
	 *
	 */
	private String time;
}