package com.yanhuo.util.msm.service.impl;

import com.aliyun.tea.TeaException;
import com.yanhuo.util.constant.OssConstant;
import com.yanhuo.util.msm.service.DmService;
import org.springframework.stereotype.Service;

@Service("dmService")
public class DmServiceImpl implements DmService {

    public static com.aliyun.dm20151123.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dm.aliyuncs.com";
        return new com.aliyun.dm20151123.Client(config);
    }


    @Override
    public void sendDm(String toAddress, String content) throws Exception {

        com.aliyun.dm20151123.Client client = createClient(OssConstant.KEY_ID, OssConstant.KEY_SECRET);
        // 初始化 Client，采用 AK&SK 鉴权访问的方式，此方式可能会存在泄漏风险，建议使用 STS 方式。鉴权访问方式请参考：https://help.aliyun.com/document_detail/378657.html
        // 获取 AK 链接：https://usercenter.console.aliyun.com
        com.aliyun.dm20151123.models.SingleSendMailRequest singleSendMailRequest = new com.aliyun.dm20151123.models.SingleSendMailRequest()
                .setAccountName("xiaozhao@ccxzvideo.top")
                .setAddressType(1)
                .setToAddress(toAddress)
                .setReplyToAddress(true)
                .setSubject("邮件发送测试")
                .setFromAlias("ccvideo")
                .setHtmlBody(content);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.singleSendMailWithOptions(singleSendMailRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
