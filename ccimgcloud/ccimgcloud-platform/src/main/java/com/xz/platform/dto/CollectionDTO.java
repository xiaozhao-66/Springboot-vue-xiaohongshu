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
public class CollectionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long uid;

	@ApiModelProperty(value = "")
	private Long collectionId;

	@ApiModelProperty(value = "")
	private Integer type;

	@ApiModelProperty(value = "")
	private Integer sort;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "")
	private Date createDate;

	@ApiModelProperty(value = "")
	private Long updater;

	@ApiModelProperty(value = "")
	private Date updateDate;
}