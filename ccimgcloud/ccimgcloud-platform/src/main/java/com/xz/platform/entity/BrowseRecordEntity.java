package com.xz.platform.entity;

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
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_browse_record")
public class BrowseRecordEntity extends BaseEntity {

	/**
	 * 用户id
	 */
	private Long uid;

	/**
	 * 图片信息id
	 */
	private Long mid;

	/**
	 * 修改用户
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 *
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}