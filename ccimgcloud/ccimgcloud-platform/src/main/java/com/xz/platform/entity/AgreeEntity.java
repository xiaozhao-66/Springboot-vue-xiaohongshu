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
@TableName("t_agree")
public class AgreeEntity extends BaseEntity {

	/**
	 * 当前点赞的用户id
	 */
	private Long uid;
	/**
	 * 点赞的类型id（点赞图片或者评论）
	 */
	private Long agreeId;

	/**
	 *  点赞图片或评论发布的用户id
	 */
	private Long agreeUid;
	/**
	 * 点赞类型
	 */
	private Integer type;
}