package com.yanhuo.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @project
 * @Description  多表联查-分页
 * @Author songwp
 * @Date 2022/8/8 10:31
 * @Version 1.0.0
 **/
public class PageUtils {

    public static<T, E> Page<T> getPages(Integer pageNo, Integer pageSize, List<T> list) {

        if(list.isEmpty()){
            return null;
        }

        Page<T> page = new Page<T>();
        int size = list.size();
        if (pageSize > size) {
            pageSize = size;
        }
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (pageNo > maxPage) {
            pageNo = maxPage;
        }
        // 当前页第一条数据下标
        int currentIndex = pageNo > 1 ? (pageNo - 1) * pageSize : 0;
        List pageList = new ArrayList<>();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && currentIndex + i < size; i++) {
            pageList.add(list.get(currentIndex + i));
        }
        page.setCurrent(pageNo).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }

    /**
     * @Description:转换为 IPage 对象
     * @Author: songwp
     */
    public static <T, E> IPage<T> copy(IPage page, List<E> sourceList, Class<T> targetClazz) {
        IPage pageResult = new Page(page.getCurrent(), page.getSize(), page.getTotal());
        pageResult.setPages(page.getPages());
        List<T> records = BeanUtil.copyToList(sourceList, targetClazz);
        pageResult.setRecords(records);
        return pageResult;
    }

    /**
     * @Description:转换为 IPage 对象
     * @Author: songwp
     */
    public static <T, E> IPage<T> copy(IPage page, Class<T> targetClazz) {
        return copy(page, page.getRecords(), targetClazz);
    }
}