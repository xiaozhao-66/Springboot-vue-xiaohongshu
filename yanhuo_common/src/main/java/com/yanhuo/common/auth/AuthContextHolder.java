package com.yanhuo.common.auth;


/**
 * @author 48423
 */
public class AuthContextHolder {

    //用户id
    private static ThreadLocal<Long> userId = new ThreadLocal<>();

    //userId操作的方法
    public static void setUserId(Long _userId) {
        userId.set(_userId);
    }

    public static Long getUserId() {
        return userId.get();
    }

}
