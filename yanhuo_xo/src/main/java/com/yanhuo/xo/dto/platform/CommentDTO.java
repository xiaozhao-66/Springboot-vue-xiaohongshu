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
@ApiModel(value = "评论")
public class CommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 *
	 */
	@ApiModelProperty(value = "评论的图片信息id")
	@NotNull(message = "视频id不能为空", groups = DefaultGroup.class)
	private Long mid;
	/**
	 *
	 */
	@ApiModelProperty(value = "发布评论的用户id")
	@NotNull(message = "用户id不能为空", groups = DefaultGroup.class)
	private Long uid;

	/**
	 *
	 */
	@ApiModelProperty(value = "评论的父id")
	private Long pid;

	/**
	 *
	 */
	@ApiModelProperty(value = "回复某一条评论的id")
	private Long replyId;

	@ApiModelProperty(value = "回复某一条评论的用户id")
	private Long replyUid;

	/**
	 *
	 */
	@ApiModelProperty(value = "评论等级")
	private Integer level;

	/**
	 *
	 */
	@ApiModelProperty(value = "评论内容")
	@NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
	private String content;
}