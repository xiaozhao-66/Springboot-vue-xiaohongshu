package com.yanhuo.util.constant;

/**
 * @author 48423
 */
public interface OssConstant {

    String ENDPOINT = "";


    String KEY_ID = "";

    String KEY_SECRET ="";

    String BUCKET_NAME = "";


    enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }
}
