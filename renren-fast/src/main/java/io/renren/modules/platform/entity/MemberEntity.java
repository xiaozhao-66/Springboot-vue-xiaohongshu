package io.renren.modules.platform.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author xiaozhao
 * @email 484235492@qq.com
 * @date 2023-07-19 15:24:23
 */
@Data
@TableName("t_user")
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
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
	private String cover;
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
