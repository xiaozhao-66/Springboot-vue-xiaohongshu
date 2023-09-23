package com.yanhuo.platform.chat;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.xo.dto.platform.MessageDTO;
import com.yanhuo.xo.vo.MessageVo;

import java.util.List;

/**
 * @author 48423
 * 已整合第三方聊天工具，这里的代码废弃
 */
public interface ChatService {

    /**
     * 增加一条聊天记录
     *
     * @param messageDTO
     */
    void addChatRecord(MessageDTO messageDTO);

    /**
     * 得到所有用户最后一条的聊天记录
     *
     * @param uid
     * @return
     */
    List<MessageVo> getChatUserList(String uid);

    /**
     * 得到固定用户的聊天记录
     *
     * @param page
     * @param limit
     * @param sendUid
     * @param acceptUid
     * @return
     */
    Page<MessageVo> getChatRecord(long page, long limit, String sendUid, String acceptUid);

    /**
     * 更改记录数量
     *
     * @param sendId
     * @param acceptId
     */
    void updateRecordCount(String sendId, String acceptId);

    /**
     * 删除聊天记录
     *
     * @param sendId
     * @param acceptId
     */
    void deleteRecord(String sendId, String acceptId);
}
