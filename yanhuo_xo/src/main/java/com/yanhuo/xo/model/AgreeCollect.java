package com.yanhuo.xo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "点赞收藏")
@TableName("t_agree_collect")
public class AgreeCollect extends BaseEntity {

    private Long uid;

    private Long agreeCollectId;

    private Long agreeCollectUid;

    private Integer type;

}
