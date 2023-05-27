package com.xz.manage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xz.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Data
@TableName("t_user")
public class UserEntity extends BaseEntity {


    /**
     *
     */
    private Long userId;
    /**
     * 
     */
	private String username;
    /**
     * 
     */
	private String password;
    /**
     * 
     */
	private String avatar;
    /**
     * 
     */
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
	private Long updater;
    /**
     * 
     */
	private Date updateDate;
}