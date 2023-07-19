package com.yanhuo.xo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_tag")
public class Tag extends BaseEntity {

    /**
     * 数量
     */
    private Long count;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
}