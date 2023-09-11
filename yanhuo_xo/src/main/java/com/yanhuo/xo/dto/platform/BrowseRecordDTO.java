package com.yanhuo.xo.dto.platform;

import com.yanhuo.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@ApiModel(value = "浏览记录")
public class BrowseRecordDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "uid不能为空", groups = DefaultGroup.class)
	private Long uid;

	@NotNull(message = "uid不能为空", groups = DefaultGroup.class)
	private Long mid;
}