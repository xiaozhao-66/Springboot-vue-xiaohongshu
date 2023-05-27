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
@TableName("t_user_other_login_relation")
public class UserOtherLoginRelationEntity extends BaseEntity {


    /**
     *
     */
    private Long uid;
    /**
     *
     */
    private String otherUserId;
    /**
     *
     */
    private String otherUsername;
    /**
     *
     */
    private String otherAvatar;
    /**
     *
     */
    private String otherToken;
}