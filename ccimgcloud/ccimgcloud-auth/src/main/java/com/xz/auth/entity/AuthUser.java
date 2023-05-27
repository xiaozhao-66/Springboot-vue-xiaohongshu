package com.xz.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author 48423
 */
@Data
@TableName("t_user")
public class AuthUser extends BaseEntity {


    private Long userId;

    private String username;

    private String password;

    private String avatar;

    private Integer gender;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private String birthday;
    /**
     *
     */
    private String address;
    /**
     *
     */
    private String cover;


    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
