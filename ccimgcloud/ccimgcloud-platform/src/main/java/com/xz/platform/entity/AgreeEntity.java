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

	private Long uid;
	/**
	 *
	 */
	private Long agreeId;

	/**
	 * 给点赞的用户id
	 */
	private Long agreeUid;
	/**
	 *
	 */
	private Integer type;
}