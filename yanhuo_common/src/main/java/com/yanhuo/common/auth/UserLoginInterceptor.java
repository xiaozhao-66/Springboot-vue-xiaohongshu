package com.yanhuo.common.auth;

import com.alibaba.fastjson.JSON;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.auth.AuthConstant;
import com.yanhuo.common.entity.UserLogin;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.JwtUtils;
import com.yanhuo.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class UserLoginInterceptor implements HandlerInterceptor {

    private static final long TIME = 60 * 60 * 24;
    private RedisUtils redisUtils;

    public UserLoginInterceptor(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler){

        String token = request.getHeader(Constant.FRONT_TOKEN_HEADER);

        //判断token不为空
        if(!StringUtils.isEmpty(token)) {
            //从token获取userId
            String userId = JwtUtils.getUserId(token);
            Long expire = redisUtils.getExpire(Constant.FRONT_TOKEN_HEADER + userId);
            UserLogin userLogin = JSON.parseObject(redisUtils.get(AuthConstant.USER_KEY + userId), UserLogin.class);
            if(expire<=0){
                throw new YanHuoException(ResultCodeEnum.TOKEN_FAIL.getMessage(),ResultCodeEnum.TOKEN_FAIL.getCode());
            } else if(expire<TIME){
                String newToken = JwtUtils.getJwtToken(String.valueOf(userLogin.getId()), userLogin.getPassword());
                redisUtils.setEx(Constant.FRONT_TOKEN_HEADER+userLogin.getId(),newToken,2, TimeUnit.DAYS);
            }

            if(userLogin!=null){
                AuthContextHolder.setUserId(userLogin.getId());
                return true;
            }

        }
        throw new YanHuoException(ResultCodeEnum.TOKEN_FAIL.getMessage(),ResultCodeEnum.TOKEN_FAIL.getCode());
    }
}
