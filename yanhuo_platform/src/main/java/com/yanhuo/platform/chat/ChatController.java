package com.yanhuo.platform.chat;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.TokenServerAssistant;
import com.yanhuo.xo.dto.platform.MessageDTO;
import com.yanhuo.xo.vo.MessageVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 小赵
 * (已废弃)已整合第三方聊天工具
 */
@RestController
@RequestMapping("/api/platform/chat")
@Api(tags="聊天模块")
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 得到zim的token信息（如果使用zim组件）
     *
     * @param userId 用户的id
     */
    @RequestMapping("/getZimToken")
    private Result<?> getZimToken(String userId) {
        TokenServerAssistant.TokenInfo tokenInfo = TokenServerAssistant.generateToken04(1562974438, userId, "516253e568dce2b1739b9c4019277309", 300, "");
        return Result.ok(tokenInfo.data);
    }

    /**
     * 增加聊天记录
     *
     * @param messageDTO
     * @return
     */
    @RequestMapping("addChatRecord")
    public Result<?> addChatRecord(@RequestBody MessageDTO messageDTO) {
        chatService.addChatRecord(messageDTO);
        return Result.ok(null);
    }

    /**
     * 得到聊天记录
     *
     * @param page
     * @param limit
     * @param sendUid
     * @param acceptUid
     * @return
     */
    @RequestMapping("getChatRecord/{page}/{limit}")
    public Result<?> getChatRecord(@PathVariable long page, @PathVariable long limit, String sendUid, String acceptUid) {
        Page<MessageVo> pageInfo = chatService.getChatRecord(page, limit, sendUid, acceptUid);
        return Result.ok(pageInfo);
    }


    /**
     * 得到聊天的用户列表
     *
     * @param uid
     * @return
     */
    @RequestMapping("getChatUserList")
    public Result<?> getChatUserList(String uid) {
        List<MessageVo> list = chatService.getChatUserList(uid);
        return Result.ok(list);
    }

    /**
     * 更新聊天
     *
     * @param sendId
     * @param acceptId
     * @return
     */
    @RequestMapping("updateRecordCount")
    public Result<?> updateRecordCount(String sendId, String acceptId) {
        chatService.updateRecordCount(sendId, acceptId);
        return Result.ok(null);
    }

    /**
     * 删除聊天记录
     *
     * @param sendId
     * @param acceptId
     * @return
     */
    @RequestMapping("deleteRecord")
    public Result<?> deleteRecord(String sendId, String acceptId) {
        chatService.deleteRecord(sendId, acceptId);
        return Result.ok(null);
    }
}
