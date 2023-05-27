package com.xz.platform.dto;

import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.common.validate.InValues;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class AgreeDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "")
	@NotNull(message = "uid不能为空", groups = DefaultGroup.class)
	private Long uid;

	@ApiModelProperty(value = "")
	@NotNull(message = "点赞id不能为空", groups = DefaultGroup.class)
	private Long agreeId;

	@ApiModelProperty(value = "")
	@NotNull(message = "给他人点赞id不能为空", groups = DefaultGroup.class)
	private Long agreeUid;


	//0代表点赞评论，1代表点赞图片
	@ApiModelProperty(value = "")
	@InValues(vals = {0, 1}, groups = DefaultGroup.class)
	private Integer type;
}