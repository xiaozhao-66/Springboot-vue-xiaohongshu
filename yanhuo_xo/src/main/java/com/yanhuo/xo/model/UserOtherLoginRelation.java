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
@TableName("t_user_other_login_relation")
public class UserOtherLoginRelation extends BaseEntity {


    /**
     * 用户id
     */
    private Long uid;
    /**
     * 第三方登录的用户id
     */
    private String otherUserId;
    /**
     * 第三方登录的用户名
     */
    private String otherUsername;
    /**
     * 第三方登录的用户头像
     */
    private String otherAvatar;
    /**
     *第三方登录的token
     */
    private String otherToken;
}