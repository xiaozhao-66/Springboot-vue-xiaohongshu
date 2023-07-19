package com.yanhuo.search.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.search.service.EsService;
import com.yanhuo.xo.dto.search.ImgDetailSearchDTO;
import com.yanhuo.xo.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 48423
 */
@RequestMapping("/api/search/imgDetailSearch")
@RestController
public class ImgDetailEsController {

    @Autowired
    EsService esService;

    /**
     * 批量增加数据
     *
     * @param dataList
     * @throws IOException
     */
    @RequestMapping("addBulkData")
    public void addBulkData(@RequestBody List<ImgDetailVo> dataList) throws IOException {
        esService.addbulkData(dataList);
    }


    /**
     * es数据查找
     *
     * @param page
     * @param limit
     * @param imgDetailSearchDTO
     * @return
     * @throws IOException
     */
    @RequestMapping("esSearch/{page}/{limit}")
    public Result<?> esSearch(@PathVariable long page, @PathVariable long limit, @RequestBody ImgDetailSearchDTO imgDetailSearchDTO) throws IOException {
        HashMap<String, Object> map = esService.esSearch(page, limit, imgDetailSearchDTO);
        return Result.ok(map);
    }

    /**
     * 分页查询数据
     *
     * @param page
     * @param limit
     * @param type
     * @return
     * @throws IOException
     */
    @RequestMapping("getPage/{page}/{limit}/{type}")
    public List<ImgDetailVo> getPage(@PathVariable long page, @PathVariable long limit, @PathVariable Integer type) throws IOException {
        return esService.getPage(page, limit, type);
    }

    /**
     * 查询所有数据
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("esSearchList")
    public List<ImgDetailVo> esSearchList() throws IOException {
        return esService.esSearchList();
    }

    /**
     * 新增一条数据
     *
     * @param imgDetailSearchVo
     * @throws IOException
     */
    @RequestMapping("addData")
    public void addData(@RequestBody ImgDetailVo imgDetailSearchVo) throws IOException {
        esService.addData(imgDetailSearchVo);
    }

    /**
     * 删除索引中的一条数据
     *
     * @param id
     * @throws IOException
     */
    @RequestMapping("delData/{id}")
    public void delData(@PathVariable String id) throws IOException {
        esService.delData(id);
    }

    /**
     * 修改数据
     *
     * @param imgDetailSearchVo
     * @throws IOException
     */
    @RequestMapping("update")
    public void update(@RequestBody ImgDetailVo imgDetailSearchVo) throws IOException {
        esService.update(imgDetailSearchVo);
    }
}
