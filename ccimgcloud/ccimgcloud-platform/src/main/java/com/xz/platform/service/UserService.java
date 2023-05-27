package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.UserDTO;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.vo.FollowVo;
import com.xz.platform.vo.TrendVo;
import com.xz.platform.vo.UserVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface UserService extends CrudService<UserEntity, UserDTO> {

    Page<TrendVo> getTrendByUser(long page, long limit, String userId);

    UserVo getUserInfo(String uid);

    Page<FollowVo> searchUser(long page , long limit, String keyword, String uid);

    UserEntity updateUser(UserEntity userEntity);

    UserVo searchUserByUsername(String username);
}