package com.yanhuo.xo.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlbumState implements Serializable {

    private Long aid;

    private Long collectionCount;
}
