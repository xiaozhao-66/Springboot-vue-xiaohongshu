package com.yanhuo.util.msm.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.expression.ExpressionException;
import com.alibaba.fastjson.JSONObject;
import com.yanhuo.util.constant.OssConstant;
import com.yanhuo.util.msm.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("msmService")
public class MsmServiceImpl implements MsmService {

    @Override
    public String sendMsm(String phone) throws Exception {
        String code = RandomUtil.randomNumbers(4);
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setEndpoint("dysmsapi.aliyuncs.com")
                // 必填，您的 AccessKey ID
                .setAccessKeyId(OssConstant.KEY_ID)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(OssConstant.KEY_SECRET);
        com.aliyun.dysmsapi20170525.Client client = new com.aliyun.dysmsapi20170525.Client(config);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName("我的谷粒学院在线网站")
                .setTemplateCode("SMS_198692078")
                .setTemplateParam(JSONObject.toJSONString(param));
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            client.sendSmsWithOptions(sendSmsRequest, runtime);
            return code;
        } catch (Exception e) {
            throw new ExpressionException("发送短信失败");
        }
    }
}
