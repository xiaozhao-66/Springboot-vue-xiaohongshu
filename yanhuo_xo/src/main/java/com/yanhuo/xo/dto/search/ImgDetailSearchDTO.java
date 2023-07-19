package com.yanhuo.xo.dto.search;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 48423
 */
@Data
public class ImgDetailSearchDTO implements Serializable {


    private String keyword;

    //0是指全部 1是指点赞排序 2是指时间排序
    private Integer type;
}
