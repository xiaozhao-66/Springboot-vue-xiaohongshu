package com.xz.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class UserEntity {

    /**
     * 
     */
	private Long id;
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
	private Long creator;
    /**
     * 
     */
	private Date createDate;
    /**
     * 
     */
	private Long updater;
    /**
     * 
     */
	private Date updateDate;
}