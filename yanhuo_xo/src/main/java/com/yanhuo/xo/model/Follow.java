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
@TableName("t_follow")
public class Follow extends BaseEntity {


    /**
     * 用户id
     */
    private Long uid;
    /**
     * 关注的用户id
     */
    private Long fid;
}