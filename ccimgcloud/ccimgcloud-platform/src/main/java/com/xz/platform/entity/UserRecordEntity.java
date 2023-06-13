package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

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
     * 用户id
     */
    private Long uid;
    /**
     * 动态数量
     */
    private Long trendCount;
    /**
     * 关注的数量
     */
    private Long followCount;
    /**
     * 粉丝数量
     */
    private Long fanCount;

    /**
     * 新关注的用户数量
     */
    private Long addFollowCount;
    /**
     * 没有回复的数量
     */
    private Long noreplyCount;
    /**
     * 新点赞和收藏的数量
     */
    private Long agreeCollectionCount;


    /**
     * （暂无作用）
     */
    private Long nochatCount;
}