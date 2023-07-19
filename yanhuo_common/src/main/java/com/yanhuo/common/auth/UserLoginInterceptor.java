package com.yanhuo.common.auth;

import com.alibaba.fastjson.JSON;
import com.yanhuo.common.constant.auth.AuthConstant;
import com.yanhuo.common.entity.UserLogin;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JwtUtils;
import com.yanhuo.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    private RedisUtils redisUtils;

    public UserLoginInterceptor(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {


        return this.getUserLoginVo(request);

    }

    private boolean getUserLoginVo(HttpServletRequest request) {
        //从请求头获取token
        String token = request.getHeader("Jwt_token");

        //判断token不为空
        if(!StringUtils.isEmpty(token)) {
            //从token获取userId
            String userId = JwtUtils.getUserId(token);

            //根据userId到Redis获取用户信息
            UserLogin userLogin = JSON.parseObject(redisUtils.get(AuthConstant.USER_KEY + userId),UserLogin.class);

            //获取数据放到ThreadLocal里面
            if(userLogin!=null) {
                AuthContextHolder.setUserId(userLogin.getId());
                return true;
            }

        }
        return false;
    }
}
