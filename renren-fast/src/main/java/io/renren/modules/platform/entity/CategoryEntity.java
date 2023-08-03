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
@TableName("t_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Long pid;
	/**
	 * 
	 */
	private Integer sort;
	/**
	 * 
	 */
	private Long count;
	/**
	 * 
	 */
	private String description;
	/**
	 * 分类的封面，如果是一级分类就是随便看看的封面，二级分类则是主封面
	 */
	private String cover;
	/**
	 * 热门封面
	 */
	private String hotCover;
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
