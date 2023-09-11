package com.yanhuo.xo.dto.platform;

import com.yanhuo.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@ApiModel(value = "专辑")
public class AlbumDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "专辑id")
	private Long id;

	@ApiModelProperty(value = "专辑名称")
	@NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
	private String name;

	@ApiModelProperty(value = "专辑发布的用户id")
	@NotNull(message = "用户id不能为空", groups = DefaultGroup.class)
	private Long uid;

	@ApiModelProperty(value = "专辑封面")
	private String cover;

	@ApiModelProperty(value = "专辑排序")
	private Integer sort;
}