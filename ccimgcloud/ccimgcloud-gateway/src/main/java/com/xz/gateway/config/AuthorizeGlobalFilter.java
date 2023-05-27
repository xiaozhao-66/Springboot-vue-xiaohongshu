package com.xz.gateway.config;

import com.xz.gateway.common.redis.RedisUtils;
import com.xz.gateway.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AuthorizeGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisUtils redisUtils;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();


        // 原始请求地址: 127.0.0.1:8800/springCloudGateway/auth/login/doLogin
        // 转化后的地址: /bffAuth/login/doLogin
        String reqPath = serverHttpRequest.getURI().getPath();

        // 全局白名单地址,例如：注册、登陆、忘记密码发送短信等等
        List<String> whiteUrlList = new ArrayList<>();
        whiteUrlList.add("/api/auth/**");
        whiteUrlList.add("/api/recommend/**");
        whiteUrlList.add("/api/utils/**");
        whiteUrlList.add("/api/platform/imgdetails/getOne");
        whiteUrlList.add("/api/platform/imgdetails/getHot");
        whiteUrlList.add("/api/platform/imgdetails/addBulkData");
        whiteUrlList.add("/api/platform/category/**");
        whiteUrlList.add("/api/search/**");
        whiteUrlList.add("/api/admin/**");

        //   /login/**      匹配 /login 开头
        //   /trip/api/*x   匹配 /trip/api/x，/trip/api/ax，/trip/api/abx ；但不匹配 /trip/abc/x
        //   /trip/a/a?x    匹配 /trip/a/abx；但不匹配 /trip/a/ax，/trip/a/abcx
        //   /**/api/alie   匹配 /trip/api/alie，/trip/dax/api/alie；但不匹配 /trip/a/api
        //   /**/*.htmlm    匹配所有以.htmlm结尾的路径

        PathMatcher pathMatcher = new AntPathMatcher();
        for (String authIgnoreTemp : whiteUrlList) {
            if (!StringUtils.isEmpty(authIgnoreTemp) && pathMatcher.match(authIgnoreTemp, reqPath)) {
                AuthorizeGlobalFilter.log.info("MyAuthorizeGlobalFilter 全局认证过滤器 ---- 地址无需登陆" + reqPath);
                return chain.filter(exchange);
            }
        }
        AuthorizeGlobalFilter.log.info("MyAuthorizeGlobalFilter 全局认证过滤器 ---- 地址需要登陆" + reqPath);
        AuthorizeGlobalFilter.log.info("请求头信息" + serverHttpRequest.getHeaders());

        String token = serverHttpRequest.getHeaders().getFirst("Jwt_token");

        // 非空校验
        if (StringUtils.isEmpty(token)) {
            // 拦截，提示未授权错误，401
            //serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 拦截结束请求
            AuthorizeGlobalFilter.log.error("TOKEN缺失");
            return serverHttpResponse.setComplete();
        }


        if (!JwtUtils.checkToken(token)) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            AuthorizeGlobalFilter.log.error("TOKEN失效");
            return serverHttpResponse.setComplete();
        }

        // 校验token，获取用户的登录对象
        try {

            String key = "user:" + JwtUtils.getUserId(token);

            String curUser = redisUtils.get(key);

            if (StringUtils.isEmpty(curUser)) {
                AuthorizeGlobalFilter.log.info("当前用户已退出登录");
                return serverHttpResponse.setComplete();
            } else {
                return chain.filter(exchange);
            }

            // redis中校验登录状态
//            String versionKey = MessageFormat.format(SysKeyEnum.LOGIN_USER_VERSION.getKey(), SysKeyConstant.PROJECTNAME, baseLoginUserInfo.getUserAccount());
//            if (!redissonSingleServiceString.isExists(versionKey)) {
//                // redis中没有当前key，则表示登录超时了，需要重写登录
//                return MonoResponseUtil.getVoidMono(serverHttpResponse, new BaseResult<String>().fail(SysStateEnum.RESPONSE_STATUS_FALSE_TOKEN_TIMEOUT.getDescribe()));
//            } else {
//                // 判断当前的version和redis中的version是否一致，不一致则表示当前已经有人登录，需要重新登录
//                String tokenVersion = redissonSingleServiceString.getKey(versionKey);
//                if (!baseLoginUserInfo.getVersion().equals(tokenVersion)) {
//                    return MonoResponseUtil.getVoidMono(serverHttpResponse, new BaseResult<String>().fail(SysStateEnum.RESPONSE_STATUS_FALSE_TOKEN_EXISTS.getDescribe()));
//                }
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
