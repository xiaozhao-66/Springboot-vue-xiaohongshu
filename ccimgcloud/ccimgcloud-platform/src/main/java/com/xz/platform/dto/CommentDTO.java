package com.xz.platform.dto;

import com.xz.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class CommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 *
	 */
	@NotNull(message = "视频id不能为空", groups = DefaultGroup.class)
	private Long mid;
	/**
	 *
	 */
	@NotNull(message = "用户id不能为空", groups = DefaultGroup.class)
	private Long uid;

	/**
	 *
	 */
	@NotBlank(message = "用户名不能为空", groups = DefaultGroup.class)
	private String username;

	/**
	 *
	 */
	@NotBlank(message = "头像不能为空", groups = DefaultGroup.class)
	private String avatar;
	/**
	 *
	 */

	private Long pid;

	/**
	 *
	 */
	private Long replyId;

	/**
	 *
	 */
	private String replyName;
	/**
	 *
	 */

	private Integer level;

	/**
	 *
	 */
	@NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
	private String content;
}