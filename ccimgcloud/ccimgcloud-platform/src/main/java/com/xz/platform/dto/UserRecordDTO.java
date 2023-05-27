package com.xz.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@ApiModel(value = "")
public class UserRecordDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "")
	private Long uid;

	@ApiModelProperty(value = "")
	private Long trendCount;

	@ApiModelProperty(value = "")
	private Long followCount;

	@ApiModelProperty(value = "")
	private Long fanCount;

	@ApiModelProperty(value = "")
	private Long replyCount;

	@ApiModelProperty(value = "")
	private Long chatCount;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "")
	private Date createDate;
}