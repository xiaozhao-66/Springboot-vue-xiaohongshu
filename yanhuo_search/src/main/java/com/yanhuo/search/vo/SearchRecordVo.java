package com.yanhuo.search.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRecordVo implements Serializable {

    private String id;

    private String keyword;

    private String highLightKeyword;

    private Integer count;

    //创建时间
    private String time;
}
