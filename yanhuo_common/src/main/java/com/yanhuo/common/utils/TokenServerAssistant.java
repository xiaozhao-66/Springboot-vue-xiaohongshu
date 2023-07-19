package com.yanhuo.common.utils;


import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author xiaozhao  484235492@qq.com
 * 这是使用第三方聊天工具所需要使用的类
 */
public class TokenServerAssistant {
    static final private String VERSION_FLAG = "04";
    static final private int IV_LENGTH = 16;
    static final private String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 通过此变量控制在生成鉴权 token 过程中是否打印控制台信息
     */
    static public boolean VERBOSE = false;

    /**
     * 权限位定义
     */
    static final public String PrivilegeKeyLogin = "1"; // 登录

    static final public String PrivilegeKeyPublish = "2"; // 推流

    /**
     * 权限开关定义
     */
    static final public int PrivilegeEnable = 1; // 开启

    static final public int PrivilegeDisable = 0; // 关闭

    static public enum ErrorCode {
        /**
         * 生成鉴权 token 成功
         */
        SUCCESS(0),
        /**
         * 传入 appId 参数错误
         */
        ILLEGAL_APP_ID(1),
        /**
         * 传入 userId 参数错误
         */
        ILLEGAL_USER_ID(3),
        /**
         * 传入 secret 参数错误
         */
        ILLEGAL_SECRET(5),
        /**
         * 传入 effectiveTimeInSeconds 参数错误
         */
        ILLEGAL_EFFECTIVE_TIME(6),
        /**
         * 其它未定义错误
         */
        OTHER(-1);

        private ErrorCode(int code) { this.value = code; }

        public int value;
    }

    static public class ErrorInfo {
        public ErrorCode code;
        public String message;

        ErrorInfo() {
            code = ErrorCode.SUCCESS;
            message = "";
        }

        @Override
        public String toString() {
            return "{\"code\": " + code.value + ", \"message\": \"" + message + "\"}";
        }
    }

    /**
     * token 结构体
     */
    static public class TokenInfo {
        /**
         * 根据提供的内容生成的 token 主体
         */
        public String data = "";

        /**
         * 错误信息
         */
        public ErrorInfo error;

        TokenInfo() { this.error = new ErrorInfo(); }

        @Override
        public String toString() {
            return "TokenInfo {\"error\": " + error + ", \"data\": \"" + data + "\"}";
        }
    }

    private TokenServerAssistant() {}

    /**
     * 根据所提供的参数列表生成用于与即构服务端通信的鉴权 token
     *
     * @param appId                  Zego派发的数字ID, 各个开发者的唯一标识
     * @param userId                 用户 ID
     * @param secret                 由即构提供的与 appId 对应的密钥，请妥善保管，切勿外泄
     * @param effectiveTimeInSeconds token 的有效时长，单位：秒
     * @return 返回 token 内容，在使用前，请检查 error 字段是否为 SUCCESS
     */
    static public TokenInfo generateToken04(long appId, String userId, String secret,
                                            int effectiveTimeInSeconds, String payload) {
        TokenInfo token = new TokenInfo();

        // check the appId
        if (appId == 0) {
            token.error.code = ErrorCode.ILLEGAL_APP_ID;
            token.error.message = "illegal appId";
            debugInfo("illegal appId");
            return token;
        }

        // check the userId
        if (userId == null || userId == "" || userId.length() > 64) {
            token.error.code = ErrorCode.ILLEGAL_USER_ID;
            token.error.message = "illegal userId";
            debugInfo("userId can't empty and must no more than 64 characters");
            return token;
        }

        // check the secret
        if (secret == null || secret == "" || secret.length() != 32) {
            token.error.code = ErrorCode.ILLEGAL_SECRET;
            token.error.message = "illegal secret";
            debugInfo("secret must 32 characters");
            return token;
        }

        // check the effectiveTimeInSeconds
        if (effectiveTimeInSeconds <= 0) {
            token.error.code = ErrorCode.ILLEGAL_EFFECTIVE_TIME;
            token.error.message = "effectiveTimeInSeconds must > 0";
            debugInfo("effectiveTimeInSeconds must > 0");
            return token;
        }

        debugInfo("generate random IV ...");
        byte[] ivBytes = new byte[IV_LENGTH];
        String iv = "cceutxv9vrhfnx0r";
        ivBytes = iv.getBytes();
        ThreadLocalRandom.current().nextBytes(ivBytes);

        JSONObject json = new JSONObject();
        json.put("app_id", appId);
        json.put("user_id", userId);

        long nowTime = System.currentTimeMillis() / 1000;
        long expire_time = nowTime + effectiveTimeInSeconds;
        json.put("ctime", nowTime);
        json.put("expire", expire_time);
        int nonce = new Random().nextInt();
        json.put("nonce", nonce);
        json.put("payload", payload);
        String content = json.toString();
        System.out.println("current nonce: " + nonce);

        try {
            debugInfo("encrypt content ...");
            byte[] contentBytes = encrypt(content.getBytes("UTF-8"), secret.getBytes(), ivBytes);

            ByteBuffer buffer = ByteBuffer.wrap(new byte[contentBytes.length + IV_LENGTH + 12]);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putLong(expire_time);     // 8 bytes
            packBytes(ivBytes, buffer);      // IV_LENGTH + 2 bytes
            packBytes(contentBytes, buffer); // contentBytes.length + 2 bytes

            debugInfo("serialize with base64 ...");
            token.data = VERSION_FLAG + Base64.getEncoder().encodeToString(buffer.array());

            token.error.code = ErrorCode.SUCCESS;
        } catch (Exception e) {
            debugInfo("generate token failed: " + e);
            token.error.code = ErrorCode.OTHER;
            token.error.message = "" + e;
        }

        return token;
    }

    static private byte[] encrypt(byte[] content, byte[] secretKey, byte[] ivBytes)
            throws Exception {
        if (secretKey == null || secretKey.length != 32) {
            throw new IllegalArgumentException("secret key's length must be 32 bytes");
        }

        if (ivBytes == null || ivBytes.length != 16) {
            throw new IllegalArgumentException("ivBytes's length must be 16 bytes");
        }

        if (content == null) {
            content = new byte[] {};
        }
        SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        return cipher.doFinal(content);
    }

    static private void packBytes(byte[] buffer, ByteBuffer target) {
        target.putShort((short)buffer.length);
        target.put(buffer);
    }

    static private void debugInfo(String info) {
        if (VERBOSE) {
            System.out.println(info);
        }
    }
}
