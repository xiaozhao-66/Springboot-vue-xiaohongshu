package com.yanhuo.xo.dto.platform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@ApiModel(value = "消息")
public class MessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "发送方的id")
	private Long sendId;

	@ApiModelProperty(value = "接收方的id")
	private Long acceptId;

	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "时间")
	private String time;
}