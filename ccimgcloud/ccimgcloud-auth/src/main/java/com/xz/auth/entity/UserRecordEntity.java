package com.xz.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_record")
public class UserRecordEntity extends BaseEntity {

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
    /**
     *
     */
    private Long replyCount;
    /**
     *
     */
    private Long chatCount;

}
