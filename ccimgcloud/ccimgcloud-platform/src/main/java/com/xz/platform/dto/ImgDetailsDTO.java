package com.xz.platform.dto;

import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.common.validator.group.UpdateGroup;
import com.xz.platform.entity.AlbumEntity;
import com.xz.platform.entity.TagEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Data
@ApiModel(value = "")
public class ImgDetailsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	@Null(message = "id为空", groups = AddGroup.class)
	@NotNull(message = "id不能为空", groups = UpdateGroup.class)
	private Long id;

	@ApiModelProperty(value = "")
	@NotNull(message = "分类id不能为空", groups = DefaultGroup.class)
	private Long categoryId;


	@ApiModelProperty(value = "")
	private Long categoryPid;

	@ApiModelProperty(value = "")
	@NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
	private String content;

	@ApiModelProperty(value = "")
	@NotBlank(message = "封面不能为空", groups = DefaultGroup.class)
	private String cover;

	@ApiModelProperty(value = "")
	@NotNull(message = "用户id不能为空", groups = DefaultGroup.class)
	private Long userId;

	@ApiModelProperty(value = "")
	@NotNull(message = "专辑id不能为空", groups = DefaultGroup.class)
	private Long albumId;

	@NotNull(message = "album不能为空", groups = UpdateGroup.class)
	private AlbumEntity album;

	@ApiModelProperty(value = "")
	@NotBlank(message = "图片地址不能为空", groups = DefaultGroup.class)
	private String imgsUrl;

	@Max(value = 9, message = "图片数量最多为9", groups = DefaultGroup.class)
	private Integer count;

	@ApiModelProperty(value = "")
	private Integer sort;

	@ApiModelProperty(value = "")
	@Size(min = 1, max = 5, message = "list的Size在[1,5]")
	private List<TagEntity> tags;

	@ApiModelProperty(value = "")
	private Integer status;

	@ApiModelProperty(value = "")
	private Long agreeCount;

	@ApiModelProperty(value = "")
	private Long collectionCount;


	@ApiModelProperty(value = "")
	private Long commentCount;
}