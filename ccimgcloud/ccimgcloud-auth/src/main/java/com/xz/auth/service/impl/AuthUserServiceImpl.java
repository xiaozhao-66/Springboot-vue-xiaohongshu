package com.xz.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.auth.constant.AuthConstant;
import com.xz.auth.dao.UserOtherLoginRelationDao;
import com.xz.auth.dao.UserRecordDao;
import com.xz.auth.dto.UserOtherLoginRelationDTO;
import com.xz.auth.entity.UserOtherLoginRelationEntity;
import com.xz.auth.entity.UserRecordEntity;
import com.xz.auth.dao.AuthUserDao;
import com.xz.auth.dto.AuthUserDTO;
import com.xz.auth.entity.AuthUser;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.RedisUtils;
import com.xz.auth.service.AuthUserService;
import com.xz.common.constant.Constant;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 48423
 */
@Service
public class AuthUserServiceImpl extends BaseServiceImpl<AuthUserDao, AuthUser> implements AuthUserService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    UserOtherLoginRelationDao userOtherLoginRelationDao;

    private static final String RES = "res";
    private static final String MESSAGE = "message";
    private static final String USER_KEY = "user:";
    private static final String USER_INFO = "userInfo";
    private static final String CODE = "code";
    private static final String EXPIRATION = "expiration";
    private static final String DEFAULT_AVATAR = "https://foruda.gitee.com/avatar/1677084428450863653/7573881_xzjsccz_1604058944.png!avatar200";
    private static final String DEFAULT_COVER = "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/06/02/c6a167251a194484ac7b25c5e3ae366720200725103959_K8EJa.jpeg";
    private static final String DEFAULT_PASSWORD = "qwer1234";


    private boolean checkCode(AuthUserDTO authUserDTO, Map<String, Object> map) {
        String code = redisUtils.get(AuthUserServiceImpl.CODE);

        if (StringUtils.isEmpty(code)) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.CODE_EXPIRE.getValue());
            return true;
        }
        if (!code.equals(authUserDTO.getCode())) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.CODE_ERROR.getValue());

            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> login(AuthUserDTO authUserDTO) {

        Map<String, Object> map = new HashMap<>(2);
        String title = authUserDTO.getUsername();
        //查询当前用户
        AuthUser authUser = baseDao.selectOne(new QueryWrapper<AuthUser>().eq("username",title).or().eq("phone", title).or().eq("email", title));

        String s = SecureUtil.md5(authUserDTO.getPassword());
        if (ObjectUtil.isEmpty(authUser) || !s.equals(authUser.getPassword())) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.LOGIN_ERROR.getValue());
            return map;
        }
        String token = JwtUtils.getJwtToken(String.valueOf(authUser.getId()), authUser.getPassword());
        //将用户信息保存在redis中
        redisUtils.set(AuthUserServiceImpl.USER_KEY + authUser.getId(), JSON.toJSONString(authUser));

        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthUserServiceImpl.USER_INFO, authUser);
        map.put(AuthUserServiceImpl.EXPIRATION, new Date(System.currentTimeMillis() + JwtUtils.EXPIRE));
        return map;
    }

    @Override
    public AuthUser getUserInfoByToken(String token) {
        String id = JwtUtils.getUserId(token);
        return baseDao.selectById(id);
    }

    @Override
    public void loginOut(AuthUser authUser) {
        String key = AuthUserServiceImpl.USER_KEY + authUser.getId();
        redisUtils.delete(key);
    }

    @Override
    public Map<String, Object> regist(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        AuthUser currentUser = baseDao.selectOne(new QueryWrapper<AuthUser>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));
        if (currentUser == null) {

            if (checkCode(authUserDTO, map)) {
                return map;
            }

            AuthUser authUser = ConvertUtils.sourceToTarget(authUserDTO, AuthUser.class);
            authUser.setUserId(Long.valueOf(RandomUtil.randomNumbers(10)));
            authUser.setUsername(RandomUtil.randomString(12));
            authUser.setAvatar(DEFAULT_AVATAR);
            authUser.setCover(DEFAULT_COVER);
            String password = SecureUtil.md5(DEFAULT_PASSWORD);
            authUser.setPassword(password);

            baseDao.insert(authUser);


            UserRecordEntity userRecordEntity = new UserRecordEntity();
            userRecordEntity.setUid(authUser.getId());
            userRecordDao.insert(userRecordEntity);

            //往第三方登录表中插入数据
            UserOtherLoginRelationEntity userOtherLoginRelationEntity = new UserOtherLoginRelationEntity();
            userOtherLoginRelationEntity.setUid(authUser.getId());
            userOtherLoginRelationDao.insert(userOtherLoginRelationEntity);


            map.put(AuthUserServiceImpl.RES, AuthConstant.SUCCESS_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.SUCCESS_REGIST.getValue());

            String token = JwtUtils.getJwtToken(String.valueOf(authUser.getId()), authUser.getPassword());
            //将用户信息保存在redis中
            redisUtils.set(AuthUserServiceImpl.USER_KEY + authUser.getId(), JSON.toJSONString(authUser));

            map.put(Constant.FRONT_TOKEN_HEADER, token);
            map.put(AuthUserServiceImpl.USER_INFO, authUser);

            return map;

        } else {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.PHONE_EMAIL_REGIST.getValue());

            return map;
        }
    }

    @Override
    public Map<String, Object> check(AuthUserDTO authUserDTO) {
        Map<String, Object> map = new HashMap<>(2);
        AuthUser currentUser = baseDao.selectOne(new QueryWrapper<AuthUser>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));

        if (currentUser == null) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.USER_NOEXIST.getValue());
            return map;
        }

        if (authUserDTO.getType() == 0 && !String.valueOf(currentUser.getUserId()).equals(authUserDTO.getUserId())) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.USERINFO_ERROR.getValue());
            return map;
        }

        if (checkCode(authUserDTO, map)) {
            return map;
        }

        map.put(AuthUserServiceImpl.RES, AuthConstant.SUCCESS_STATUE);
        return map;
    }


    @Override
    public Boolean updatePassword(AuthUserDTO authUserDTO) {

        if (!authUserDTO.getPassword().equals(authUserDTO.getCheckPassword())) {
            return false;
        }
        String pwd = SecureUtil.md5(authUserDTO.getPassword());
        AuthUser authUser = null;
        if (authUserDTO.getId() == null) {
            authUser = baseDao.selectOne(new QueryWrapper<AuthUser>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));
        } else {
            authUser = ConvertUtils.sourceToTarget(authUserDTO, AuthUser.class);
        }
        authUser.setPassword(pwd);
        int i = baseDao.updateById(authUser);
        return i > 0;
    }

    @Override
    public Map<String, Object> loginByCode(AuthUserDTO authUserDTO) {

        Map<String, Object> map = new HashMap<>(2);
        AuthUser currentUser = baseDao.selectOne(new QueryWrapper<AuthUser>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));

        if (currentUser == null) {
            map.put(AuthUserServiceImpl.RES, AuthConstant.ERROR_STATUE.toString());
            map.put(AuthUserServiceImpl.MESSAGE, AuthConstant.ReturnMessage.PHONE_EMAIL_NO_REGIST.getValue());
        } else {

            if (checkCode(authUserDTO, map)) {
                return map;
            }
            String token = JwtUtils.getJwtToken(String.valueOf(currentUser.getId()), currentUser.getPassword());
            //将用户信息保存在redis中
            redisUtils.set(AuthUserServiceImpl.USER_KEY + currentUser.getId(), JSON.toJSONString(currentUser));
            map.put(AuthUserServiceImpl.RES, AuthConstant.SUCCESS_STATUE.toString());
            map.put(Constant.FRONT_TOKEN_HEADER, token);
            map.put(AuthUserServiceImpl.USER_INFO, currentUser);
            return map;
        }
        return map;

    }

    @Override
    public Map<String, Object> otherLogin(UserOtherLoginRelationDTO userOtherLoginRelationDTO) {
        Map<String, Object> map = new HashMap<>(2);
        //如果是第一次登录
        UserOtherLoginRelationEntity userOtherLoginRelation = userOtherLoginRelationDao.selectOne(new QueryWrapper<UserOtherLoginRelationEntity>().eq("other_user_id", userOtherLoginRelationDTO.getOtherUserId()));
        AuthUser authUser = null;
        if (userOtherLoginRelation == null) {

            //注册用户
            authUser = new AuthUser();
            authUser.setUserId(Long.valueOf(RandomUtil.randomNumbers(10)));
            authUser.setUsername(RandomUtil.randomString(12));
            authUser.setAvatar(userOtherLoginRelationDTO.getOtherAvatar());
            authUser.setCover(DEFAULT_COVER);

            baseDao.insert(authUser);

            //插入到第三方登录表中
            UserOtherLoginRelationEntity sourceUserOtherLoginRelation = ConvertUtils.sourceToTarget(userOtherLoginRelationDTO, UserOtherLoginRelationEntity.class);
            sourceUserOtherLoginRelation.setUid(authUser.getId());
            userOtherLoginRelationDao.insert(sourceUserOtherLoginRelation);

            //用户记录表中也要插入数据
            UserRecordEntity userRecordEntity = new UserRecordEntity();
            userRecordEntity.setUid(authUser.getId());
            userRecordDao.insert(userRecordEntity);

        } else {
            authUser = baseDao.selectById(userOtherLoginRelation.getUid());
        }

        String token = JwtUtils.getJwtToken(String.valueOf(authUser.getId()), authUser.getPassword());
        //将用户信息保存在redis中
        redisUtils.set(AuthUserServiceImpl.USER_KEY + authUser.getId(), JSON.toJSONString(authUser));
        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthUserServiceImpl.USER_INFO, authUser);
        return map;
    }

    @Override
    public Map<String, Object> refreshToken(AuthUser authUser) {
        HashMap<String, Object> map = new HashMap<>();
        String token = JwtUtils.getJwtToken(String.valueOf(authUser.getId()), authUser.getPassword());
        map.put(Constant.FRONT_TOKEN_HEADER, token);
        map.put(AuthUserServiceImpl.EXPIRATION, new Date(System.currentTimeMillis() + JwtUtils.EXPIRE));
        return map;
    }

    @Override
    public boolean isRegist(AuthUserDTO authUserDTO) {

        Long count = baseDao.selectCount(new QueryWrapper<AuthUser>().eq("phone", authUserDTO.getPhone()).or().eq("email", authUserDTO.getEmail()));

        return count>0;
    }

}
