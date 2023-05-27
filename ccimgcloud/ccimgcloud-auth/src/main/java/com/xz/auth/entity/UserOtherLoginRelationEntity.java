package com.xz.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("t_user_other_login_relation")
public class UserOtherLoginRelationEntity extends BaseEntity {

    private Long uid;

    private String otherUserId;

    private String otherUsername;

    private String otherAvatar;

    private String otherToken;

}
