package com.xz.search.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRecordDTO implements Serializable {

    private String id;

    private String keyWord;

    private String highLightKeyword;

    private Integer count;

    //创建时间
    private String time;
}
