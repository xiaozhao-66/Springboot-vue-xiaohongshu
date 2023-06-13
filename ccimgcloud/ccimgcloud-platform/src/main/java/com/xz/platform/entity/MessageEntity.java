package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_message")
public class MessageEntity extends BaseEntity {


	/**
	 * 发送方的用户id
	 */
	private Long sendId;
	/**
	 * 接收方的用户id
	 */
	private Long acceptId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 时间
	 */
	private String time;
}