package com.xz.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_message_user_relation")
public class MessageUserRelationEntity extends BaseEntity {

    private Long sendId;

    private Long acceptId;

    private Integer count;

    private String content;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
