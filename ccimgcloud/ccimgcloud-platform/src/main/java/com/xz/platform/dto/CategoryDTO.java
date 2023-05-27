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
 * @since 1.0.0 2023-03-13
 */
@Data
@ApiModel(value = "")
public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "")
	private String name;

	@ApiModelProperty(value = "")
	private Long pid;

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