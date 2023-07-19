package com.yanhuo.xo.dto.platform;

import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.xo.validate.InValues;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AgreeCollectDTO implements Serializable {

    @ApiModelProperty(value = "当前点赞的用户id")
    @NotNull(message = "uid不能为空", groups = DefaultGroup.class)
    private Long uid;

    @ApiModelProperty(value = "点赞的类型id")
    @NotNull(message = "点赞id不能为空", groups = DefaultGroup.class)
    private Long agreeCollectId;

    @ApiModelProperty(value = "点赞图片或评论发布的用户id")
    @NotNull(message = "给他人点赞id不能为空", groups = DefaultGroup.class)
    private Long agreeCollectUid;


    @ApiModelProperty(value = "0代表点赞评论，1代表点赞图片,2代表收藏图片,3是收藏专辑")
    @InValues(vals = {0, 1, 2, 3}, groups = DefaultGroup.class)
    private Integer type;
}
