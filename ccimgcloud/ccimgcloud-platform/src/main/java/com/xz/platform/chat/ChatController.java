package com.xz.platform.chat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.platform.common.utils.TokenServerAssistant;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.vo.MessageVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 小赵
 */
@RestController
@RequestMapping("/chat")
@Api(tags="聊天模块")
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 得到zim的token信息
     *
     * @param userId 用户的id
     */
    @RequestMapping("/getZimToken")
    private Result<?> getZimToken(String userId) {
        TokenServerAssistant.TokenInfo tokenInfo = TokenServerAssistant.generateToken04(1562974438, userId, "516253e568dce2b1739b9c4019277309", 300, "");
        return new Result<>().ok(tokenInfo.data);
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
        return new Result<>().ok();
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
        return new Result<>().ok(pageInfo);
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
        return new Result<>().ok(list);
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
        return new Result<>().ok();
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
        return new Result<>().ok();
    }
}
