package com.xz.manage.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xz.common.utils.TreeNode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CategoryVo extends TreeNode<CategoryVo> implements Serializable {

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Integer sort;

    /**
     *
     */
    private Long  creator;

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
