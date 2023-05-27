package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author 48423
 */
@Data
@TableName("t_search_record")
public class SearchRecordEntity extends BaseEntity {

    private Long uid;

    private String searchContent;
}
