package com.yanhuo.util.msm.service;

/**
 * @author 48423
 */
public interface DmService {

    /**
     * 发送邮件
     * @param toAddress
     * @param content
     * @throws Exception
     */
    void sendDm(String toAddress, String content) throws Exception;
}
