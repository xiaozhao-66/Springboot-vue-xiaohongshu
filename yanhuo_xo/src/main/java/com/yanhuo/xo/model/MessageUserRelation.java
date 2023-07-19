package com.yanhuo.xo.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_message_user_relation")
public class MessageUserRelation extends BaseEntity {

    /**
     * 发生方的用户id
     */
    private Long sendId;
    /**
     * 接收方的用户id
     */
    private Long acceptId;
    /**
     * 未查看的消息数量
     */
    private Integer count;
    /**
     * 最后一条的内容
     */
    private String content;
    /**
     * 最后一条信息的时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
