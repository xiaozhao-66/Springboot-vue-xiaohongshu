/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.yanhuo.common.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 校验工具类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class AssertUtils {

    public static void isBlank(String str, String... params) {
        isBlank(str, ResultCodeEnum.NOT_NULL.getCode(), params);
    }

    public static void isBlank(String str, Integer code, String... params) {
        if(code == null){
            throw new YanHuoException(ResultCodeEnum.NOT_NULL.getCode(), "code");
        }

        if (StringUtils.isBlank(str)) {
            throw new YanHuoException(code, params);
        }
    }

    public static void isNull(Object object, String... params) {
        isNull(object, ResultCodeEnum.NOT_NULL.getCode(), params);
    }

    public static void isNull(Object object, Integer code, String... params) {
        if(code == null){
            throw new YanHuoException(ResultCodeEnum.NOT_NULL.getCode(), "code");
        }

        if (object == null) {
            throw new YanHuoException(code, params);
        }
    }

    public static void isArrayEmpty(Object[] array, String... params) {
        isArrayEmpty(array, ResultCodeEnum.NOT_NULL.getCode(), params);
    }

    public static void isArrayEmpty(Object[] array, Integer code, String... params) {
        if(code == null){
            throw new YanHuoException(ResultCodeEnum.NOT_NULL.getCode(), "code");
        }

        if(ArrayUtil.isEmpty(array)){
            throw new YanHuoException(code, params);
        }
    }

    public static void isListEmpty(List<?> list, String... params) {
        isListEmpty(list, ResultCodeEnum.NOT_NULL.getCode(), params);
    }

    public static void isListEmpty(List<?> list, Integer code, String... params) {
        if(code == null){
            throw new YanHuoException(ResultCodeEnum.NOT_NULL.getCode(), "code");
        }

        if(CollUtil.isEmpty(list)){
            throw new YanHuoException(code, params);
        }
    }

    public static void isMapEmpty(Map map, String... params) {
        isMapEmpty(map, ResultCodeEnum.NOT_NULL.getCode(), params);
    }

    public static void isMapEmpty(Map map, Integer code, String... params) {
        if(code == null){
            throw new YanHuoException(ResultCodeEnum.NOT_NULL.getCode(), "code");
        }

        if(MapUtil.isEmpty(map)){
            throw new YanHuoException(code, params);
        }
    }
}