package com.yanhuo.common.exception;

import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.MessageUtils;
import lombok.Data;

@Data
public class YanHuoException extends RuntimeException{

    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public YanHuoException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public YanHuoException(int code, String... params) {
        super(MessageUtils.getMessage(code, params));
        this.code = code;
    }

    public YanHuoException(String msg) {
        super(msg);
        this.code = ResultCodeEnum.FAIL.getCode();
    }


    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public YanHuoException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

}
