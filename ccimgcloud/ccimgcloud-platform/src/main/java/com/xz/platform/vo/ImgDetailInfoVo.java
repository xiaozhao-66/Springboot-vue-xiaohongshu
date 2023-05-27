package com.xz.platform.vo;

import com.xz.platform.entity.AlbumEntity;
import com.xz.platform.entity.TagEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ImgDetailInfoVo implements Serializable {
    /**
     *
     */
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
    //
    private Long categoryId;
    //
    private String categoryName;
//
    private Long categoryPid;
//
    private String categoryPName;

    private String avatar;

    private String username;
//
    private AlbumEntity album;

    private List<String> imgsUrl;
//
    private List<TagEntity> tagList;
//
    private Long viewCount;

    private Long agreeCount;
//
    private Long collectionCount;

    private String time;
}
