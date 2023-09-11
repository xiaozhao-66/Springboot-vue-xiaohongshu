package com.yanhuo.xo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Data
@TableName("t_tag_img_relation")
public class TagImgRelation {

    /**
     * id
     */
    private Long id;
    /**
     * 图像信息id
     */
    private Long mid;
    /**
     * 标签的ids
     */
    private Long tid;
}