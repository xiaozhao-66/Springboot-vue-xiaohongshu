package com.yanhuo.util.msm.service;

/**
 * @author 48423
 */
public interface MsmService {

    /**
     * 发送短信
     * @param phone
     * @return
     * @throws Exception
     */
    String sendMsm(String phone) throws Exception;
}
