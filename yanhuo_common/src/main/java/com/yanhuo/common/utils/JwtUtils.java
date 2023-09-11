package com.yanhuo.common.utils;

import com.yanhuo.common.constant.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {
    //定义两个常量，1.设置过期时间 2.密钥（随机，由公司生成）
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";
    //生成token字符串，用户id和名称（可以写多个）
    public static String getJwtToken(String userId, String password){

        String JwtToken = Jwts.builder()
                //设置token的头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //设置过期时间
                .setSubject("user")
                .setIssuedAt(new Date())
                //设置刷新
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //设置token的主题部分
                .claim("userId", userId)
                .claim("password", password)
                //签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            //验证是否有效的token
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 根据token信息得到getUserId
     * @param jwtToken
     * @return
     */
    public static String getUserId(String jwtToken) {
        //验证是否有效的token
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        //得到字符串的主题部分
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userId");
    }
    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader(Constant.FRONT_TOKEN_HEADER);
            if(StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(Constant.FRONT_TOKEN_HEADER);
        if(StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        //得到字符串的主题部分
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userId");
    }
}