package com.yanhuo.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.BrowseRecordDTO;
import com.yanhuo.xo.dto.platform.ImgDetailDTO;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.vo.ImgDetailVo;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface ImgDetailService extends IService<ImgDetail> {

    /**
     * 根据id删除当前用户上传到图片
     *
     * @param idList
     * @param uid
     */
    void deleteImgs(List<String> idList, String uid);

    /**
     * 分页获取所有的图片信息
     *
     * @param page
     * @param limit
     * @return
     */
    Page<ImgDetailVo> getPage(long page, long limit);

    /**
     * 根据专辑id删除图片
     *
     * @param page
     * @param limit
     * @param albumId
     * @param type
     * @return
     */
    Page<ImgDetailVo> getAllImgByAlbum(long page, long limit, String albumId, Integer type);

    /**
     * 得到当前图片信息
     *
     * @param id
     * @return
     */
    ImgDetailVo getImgDetail(String id);

    /**
     * 发布图片信息
     *
     * @param imgDetailsDTO
     * @return
     */
    Long publish(ImgDetailDTO imgDetailsDTO);

    /**
     * 根据图片上传状态
     *
     * @param imgDetailDTO
     * @return
     */
    //void updateStatus(ImgDetailDTO imgDetailsDTO);

    Long updateImgDetail(ImgDetailDTO imgDetailDTO);

    /**
     * 获取热榜数据
     *
     * @param page
     * @param limit
     * @return
     */
    Page<ImgDetailVo> getHot(long page, long limit);

    /**
     * 得到所有的浏览记录根据用户id
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<ImgDetailVo> getAllBrowseRecordByUser(long page, long limit, String uid);

    /**
     * 增加一条浏览记录
     *
     * @param browseRecordDTO
     */
    void addBrowseRecord(BrowseRecordDTO browseRecordDTO);

    /**
     * 删除浏览记录
     *
     * @param uid
     * @param idList
     */
    void delRecord(String uid, List<String> idList);

    /**
     * 批量上传数据
     */
    void addBulkData();

    /**
     * 批量上传数据到redis中
     */
    void addBulkRedisData();
}