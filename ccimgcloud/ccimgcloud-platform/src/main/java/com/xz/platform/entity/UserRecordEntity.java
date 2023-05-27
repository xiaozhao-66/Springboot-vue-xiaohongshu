package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_user_record")
public class UserRecordEntity extends BaseEntity {


    /**
     *
     */
    private Long uid;
    /**
     *
     */
    private Long trendCount;
    /**
     *
     */
    private Long followCount;
    /**
     *
     */
    private Long fanCount;


    private Long addFollowCount;
    /**
     *
     */
    private Long noreplyCount;
    /**
     *
     */
    private Long agreeCollectionCount;


    /**
     *
     */
    private Long nochatCount;
}