package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.search.SearchRecordDTO;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;
import com.yanhuo.xo.vo.UserRecordVo;
import com.yanhuo.xo.vo.UserVo;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface UserService extends IService<User> {

    /**
     * 得到当前用户发布的所有动态
     *
     * @param page
     * @param limit
     * @param userId
     * @param type
     * @return
     */
    Page<TrendVo> getTrendByUser(long page, long limit, String userId, Integer type);


    /**
     * 查找一个用户
     *
     * @param page
     * @param limit
     * @param keyword
     * @param uid
     * @return
     */
    Page<FollowVo> searchUser(long page, long limit, String keyword, String uid);

    /**
     * 更新一个用户信息
     *
     * @param user
     * @return
     */
    User updateUser(User user);

    /**
     * 通过用户名搜索用户
     *
     * @param username
     * @return
     */
    List<UserVo> searchUserByUsername(String username);

    /**
     * 得到用户的记录（新收到的点赞，关注，评论信息）
     *
     * @param uid
     * @return
     */
    UserRecordVo getUserRecord(String uid);

    /**
     * 删除用户的记录
     *
     * @param uid
     * @param type
     */
    void clearUserRecord(String uid, Integer type);

    /**
     * 得到所有的搜索记录根据用户id
     *
     * @param uid
     * @return
     */
    List<String> getAllSearchRecord(String uid);

    /**
     * 增加一条搜索记录
     *
     * @param searchRecordDTO
     */
    void addSearchRecord(SearchRecordDTO searchRecordDTO);

    /**
     * 删除搜索记录
     *
     * @param words
     * @param uid
     */
    void deleteSearchRecord(List<String> words, String uid);

    /**
     * 批量上传用户记录数据
     *
     */
    void addBulkUserRecord();
}