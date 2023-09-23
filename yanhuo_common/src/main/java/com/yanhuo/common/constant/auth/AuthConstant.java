package com.yanhuo.common.constant.auth;

/**
 * @author 48423
 */
public interface AuthConstant {

    Integer ERROR_STATUE = 0;

    Integer SUCCESS_STATUE = 1;

    String RES = "res";

    String MESSAGE = "message";

    String USER_KEY = "user:";

    String USER_INFO = "userInfo";

    String EXPIRATION = "expiration";

    String CODE = "code:";

    String DEFAULT_AVATAR = "https://foruda.gitee.com/avatar/1677084428450863653/7573881_xzjsccz_1604058944.png!avatar200";

    String DEFAULT_COVER = "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/06/02/c6a167251a194484ac7b25c5e3ae366720200725103959_K8EJa.jpeg";

    String DEFAULT_PASSWORD = "qwer1234";

    enum ReturnMessage {
        /**
         * 暂停
         */
        LOGIN_ERROR("账号或密码错误"),
        CODE_EXPIRE("验证码过期"),
        CODE_ERROR("验证码错误"),
        PHONE_EMAIL_REGIST("手机号或邮箱已被注册"),
        PHONE_EMAIL_NO_REGIST("手机号或邮箱未被注册"),
        SUCCESS_REGIST("注册成功"),
        USER_NOEXIST("用户不存在"),
        USERINFO_ERROR("用户信息输入有误");

        private final String message;

        ReturnMessage(String message) {
            this.message = message;
        }

        public String getValue() {
            return message;
        }
    }
}
