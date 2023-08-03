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
 * @date 2023-07-19 19:42:35
 */
@Data
@TableName("t_img_detail")
public class ImgDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private String cover;
	/**
	 * 
	 */
	private Long userId;
	/**
	 * 
	 */
	private Long categoryId;
	/**
	 * 
	 */
	private Long categoryPid;
	/**
	 * 
	 */
	private String imgsUrl;
	/**
	 * 
	 */
	private Integer count;
	/**
	 * 
	 */
	private Integer sort;
	/**
	 * 
	 */
	private Integer status;
	/**
	 * 
	 */
	private Long viewCount;
	/**
	 * 
	 */
	private Long agreeCount;
	/**
	 * 
	 */
	private Long collectionCount;
	/**
	 * 
	 */
	private Long commentCount;
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
