package com.yanhuo.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.auth.dao.AuthUserDao;
import com.yanhuo.auth.dao.UserOtherLoginRelationDao;
import com.yanhuo.auth.service.AuthUserService;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.auth.AuthConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JwtUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.xo.dto.auth.AuthUserDTO;
import com.yanhuo.xo.dto.auth.UserOtherLoginRelationDTO;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.model.UserOtherLoginRelation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserDao, User> implements AuthUserService {


    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserOtherLoginRelationDao userOtherLoginRelationDao;

    @Override
    public Map<String, Object> login(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        String title = authUserDTO.getUsername();
        //查询当前用户
        User authUser = this.getOne(new QueryWrapper<User>().eq("username", title).or().eq("phone", title).or().eq("email", title));

        String s = SecureUtil.md5(authUserDTO.getPassword());
        if (ObjectUtil.isEmpty(authUser) || !s.equals(authUser.getPassword())) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.LOGIN_ERROR.getValue());
            return map;
        }
        String token = JwtUtils.getJwtToken(String.valueOf(authUser.getId()), authUser.getPassword());
        //将用户信息保存在redis中
        redisUtils.set(AuthConstant.USER_KEY + authUser.getId(), JSON.toJSONString(authUser));
        redisUtils.setEx(Constant.FRONT_TOKEN_HEADER+authUser.getId(),token,2, TimeUnit.DAYS);

        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthConstant.USER_INFO, authUser);

        return map;
    }

    private boolean checkCode(AuthUserDTO authUserDTO, Map<String, Object> map) {

        String code = "";

        if(StringUtils.isNotBlank(redisUtils.get(AuthConstant.CODE+authUserDTO.getPhone()))){
            code = redisUtils.get(AuthConstant.CODE+authUserDTO.getPhone());
        }else if(StringUtils.isNotBlank(redisUtils.get(AuthConstant.CODE+authUserDTO.getEmail()))){
            code = redisUtils.get(AuthConstant.CODE+authUserDTO.getEmail());
        }


        if (StringUtils.isEmpty(code)) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.CODE_EXPIRE.getValue());
            return true;
        }
        if (!code.equals(authUserDTO.getCode())) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.CODE_ERROR.getValue());

            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> loginByCode(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        User currentUser = this.getOne(new QueryWrapper<User>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));

        if (currentUser == null) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.PHONE_EMAIL_NO_REGIST.getValue());
        } else {

            if (checkCode(authUserDTO, map)) {
                return map;
            }
            String token = JwtUtils.getJwtToken(String.valueOf(currentUser.getId()), currentUser.getPassword());
            //将用户信息保存在redis中
            redisUtils.set(AuthConstant.USER_KEY + currentUser.getId(), JSON.toJSONString(currentUser));
            redisUtils.setEx(Constant.FRONT_TOKEN_HEADER+currentUser.getId(),token,2, TimeUnit.DAYS);
            map.put(AuthConstant.RES, AuthConstant.SUCCESS_STATUE.toString());
            map.put(Constant.FRONT_TOKEN_HEADER, token);
            map.put(AuthConstant.USER_INFO, currentUser);
            return map;
        }
        return map;
    }

    @Override
    public User getUserInfoByToken(String token) {
        String id = JwtUtils.getUserId(token);
        return this.getById(id);
    }

    @Override
    public Map<String, Object> regist(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        User currentUser = this.getOne(new QueryWrapper<User>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));
        if (currentUser == null) {

            if (checkCode(authUserDTO, map)) {
                return map;
            }

            User user = ConvertUtils.sourceToTarget(authUserDTO, User.class);
            user.setUserId(Long.valueOf(RandomUtil.randomNumbers(10)));
            user.setUsername(RandomUtil.randomString(12));
            user.setAvatar(AuthConstant.DEFAULT_AVATAR);
            user.setCover(AuthConstant.DEFAULT_COVER);
            String password = SecureUtil.md5(AuthConstant.DEFAULT_PASSWORD);
            user.setPassword(password);

            this.save(user);

            //往第三方登录表中插入数据
            UserOtherLoginRelation userOtherLoginRelation = new UserOtherLoginRelation();
            userOtherLoginRelation.setUid(user.getId());
            userOtherLoginRelationDao.insert(userOtherLoginRelation);


            map.put(AuthConstant.RES, AuthConstant.SUCCESS_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.SUCCESS_REGIST.getValue());

            String token = JwtUtils.getJwtToken(String.valueOf(user.getId()), user.getPassword());
            //将用户信息保存在redis中
            redisUtils.set(AuthConstant.USER_KEY + user.getId(), JSON.toJSONString(user));
            redisUtils.setEx(Constant.FRONT_TOKEN_HEADER+user.getId(),token,2, TimeUnit.DAYS);
            map.put(Constant.FRONT_TOKEN_HEADER, token);
            map.put(AuthConstant.USER_INFO, user);

            return map;
        } else {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.PHONE_EMAIL_REGIST.getValue());
            return map;
        }
    }

    @Override
    public boolean isRegist(AuthUserDTO authUserDTO) {
        long count = this.count(new QueryWrapper<User>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));
        return count > 0;
    }

    @Override
    public void loginOut(AuthUserDTO authUserDTO) {
        String key = AuthConstant.USER_KEY + authUserDTO.getId();
        String tokenKey = Constant.FRONT_TOKEN_HEADER+authUserDTO.getId();
        redisUtils.delete(key);
        redisUtils.delete(tokenKey);
    }

    @Override
    public Map<String, Object> check(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        User currentUser = this.getOne(new QueryWrapper<User>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));

        if (currentUser == null) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.USER_NOEXIST.getValue());
            return map;
        }

        if (authUserDTO.getType() == 0 && !currentUser.getId().equals(authUserDTO.getId())) {
            map.put(AuthConstant.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthConstant.MESSAGE, AuthConstant.ReturnMessage.USERINFO_ERROR.getValue());
            return map;
        }

        if (checkCode(authUserDTO, map)) {
            return map;
        }

        map.put(AuthConstant.RES, AuthConstant.SUCCESS_STATUE);
        return map;
    }

    @Override
    public Boolean updatePassword(AuthUserDTO authUserDTO) {
        if (!authUserDTO.getPassword().equals(authUserDTO.getCheckPassword())) {
            return false;
        }
        String pwd = SecureUtil.md5(authUserDTO.getPassword());
        User user = null;
        if (authUserDTO.getId() == null) {
            user = this.getOne(new QueryWrapper<User>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));
        } else {
            user = this.getById(authUserDTO.getId());
        }
        user.setPassword(pwd);
        return this.updateById(user);
    }

    @Override
    public Map<String, Object> otherLogin(UserOtherLoginRelationDTO userOtherLoginRelationDTO) {
        Map<String, Object> map = new HashMap<>(2);
        //如果是第一次登录
        UserOtherLoginRelation userOtherLoginRelation = userOtherLoginRelationDao.selectOne(new QueryWrapper<UserOtherLoginRelation>().eq("other_user_id", userOtherLoginRelationDTO.getOtherUserId()));
        User user = null;
        if (userOtherLoginRelation == null) {

            //注册用户
            user = new User();
            user.setUserId(Long.valueOf(RandomUtil.randomNumbers(10)));
            user.setUsername(RandomUtil.randomString(12));
            user.setAvatar(userOtherLoginRelationDTO.getOtherAvatar());
            user.setCover(AuthConstant.DEFAULT_COVER);

            this.save(user);

            //插入到第三方登录表中
            UserOtherLoginRelation sourceUserOtherLoginRelation = ConvertUtils.sourceToTarget(userOtherLoginRelationDTO, UserOtherLoginRelation.class);
            sourceUserOtherLoginRelation.setUid(user.getId());
            userOtherLoginRelationDao.insert(sourceUserOtherLoginRelation);
        } else {
            user = this.getById(userOtherLoginRelation.getUid());
        }

        String token = JwtUtils.getJwtToken(String.valueOf(user.getId()), user.getPassword());
        //将用户信息保存在redis中
        redisUtils.set(AuthConstant.USER_KEY + user.getId(), JSON.toJSONString(user));
        redisUtils.setEx(Constant.FRONT_TOKEN_HEADER+user.getId(),token,2, TimeUnit.DAYS);
        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthConstant.USER_INFO, user);
        return map;
    }

    @Override
    public Map<String, Object> refreshToken(AuthUserDTO authUserDTO) {
        HashMap<String, Object> map = new HashMap<>();
        String token = JwtUtils.getJwtToken(String.valueOf(authUserDTO.getId()), authUserDTO.getPassword());
        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthConstant.EXPIRATION, new Date(System.currentTimeMillis() + JwtUtils.EXPIRE));
        return map;
    }
}
