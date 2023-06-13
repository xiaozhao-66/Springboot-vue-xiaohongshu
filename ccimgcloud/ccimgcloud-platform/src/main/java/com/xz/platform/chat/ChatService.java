package com.xz.platform.chat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.vo.MessageVo;

import java.util.List;

/**
 * @author 48423
 */
public interface ChatService {

    void addChatRecord(MessageDTO messageDTO);

    List<MessageVo> getChatUserList(String uid);

    Page<MessageVo> getChatRecord(long page, long limit, String sendUid, String acceptUid);

    void updateRecordCount(String sendId, String acceptId);

    void deleteRecord(String sendId, String acceptId);
}
