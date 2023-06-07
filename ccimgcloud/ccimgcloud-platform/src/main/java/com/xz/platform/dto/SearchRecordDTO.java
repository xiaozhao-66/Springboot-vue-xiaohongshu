package com.xz.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * @author 48423
 */
@Data
@ApiModel(value = "搜索记录")
public class SearchRecordDTO implements Serializable {


    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "关键词")
    private String keyword;
}
