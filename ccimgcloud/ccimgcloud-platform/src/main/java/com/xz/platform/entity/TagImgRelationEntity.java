package com.xz.platform.entity;

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
public class TagImgRelationEntity {

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
    private String tagIds;
}