package com.yanhuo.xo.dto.platform;

import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.common.validator.group.UpdateGroup;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Data
@ApiModel(value = "图片信息")
public class ImgDetailDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	@Null(message = "id为空", groups = AddGroup.class)
	@NotNull(message = "id不能为空", groups = UpdateGroup.class)
	private Long id;

	@ApiModelProperty(value = "分类id")
	@NotNull(message = "分类id不能为空", groups = DefaultGroup.class)
	private Long categoryId;


	@ApiModelProperty(value = "分类父级id")
	private Long categoryPid;

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空", groups = DefaultGroup.class)
	private String content;

	@ApiModelProperty(value = "封面")
	@NotBlank(message = "封面不能为空", groups = DefaultGroup.class)
	private String cover;

	@ApiModelProperty(value = "用户id")
	@NotNull(message = "用户id不能为空", groups = AddGroup.class)
	private Long userId;

	@ApiModelProperty(value = "专辑id")
	@NotNull(message = "专辑id不能为空", groups = DefaultGroup.class)
	private Long albumId;

	@ApiModelProperty(value = "专辑")
	private Album album;

	@ApiModelProperty(value = "图片地址")
	@NotBlank(message = "图片地址不能为空", groups = DefaultGroup.class)
	private String imgsUrl;

	@ApiModelProperty(value = "图片数量")
	@Max(value = 9, message = "图片数量最多为9", groups = DefaultGroup.class)
	private Integer count;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "标签")
	@Size(min = 1, max = 5, message = "list的Size在[1,5]")
	private List<Tag> tags;

	@ApiModelProperty(value = "状态")
	private Integer status;

	@ApiModelProperty(value = "点赞数量")
	private Long agreeCount;

	@ApiModelProperty(value = "收藏数量")
	private Long collectionCount;


	@ApiModelProperty(value = "评论数量")
	private Long commentCount;

	@ApiModelProperty(value = "0增加图片，1修改图片")
	private Integer type;
}