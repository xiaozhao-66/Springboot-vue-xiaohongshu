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
@ApiModel(value = "收藏")
public class CollectionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "收藏的用户id")
	private Long uid;

	@ApiModelProperty(value = "收藏类型的id")
	private Long collectionId;

	@ApiModelProperty(value = "收藏的类型（0是图片，1是专辑）")
	private Integer type;

	@ApiModelProperty(value = "排序")
	private Integer sort;

}