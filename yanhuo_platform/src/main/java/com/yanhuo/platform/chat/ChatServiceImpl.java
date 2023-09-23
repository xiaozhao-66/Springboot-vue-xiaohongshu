package com.yanhuo.platform.chat;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.DateUtils;
import com.yanhuo.common.utils.PageUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.dao.MessageDao;
import com.yanhuo.platform.dao.MessageUserRelationDao;
import com.yanhuo.platform.dao.UserDao;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.MessageDTO;
import com.yanhuo.xo.model.Message;
import com.yanhuo.xo.model.MessageUserRelation;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.yanhuo.common.utils.DateUtils.DATE_TIME_PATTERN;

/**
 * @author xiaozhao
 * 已整合第三方聊天工具，这里的代码废弃
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    MessageUserRelationDao messageUserRelationDao;

    @Autowired
    UserDao userDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addChatRecord(MessageDTO messageDTO) {
        //往数据库里面添加记录，
        Message messageEntity = ConvertUtils.sourceToTarget(messageDTO, Message.class);
        messageEntity.setTime(String.valueOf(System.currentTimeMillis()));
        messageDao.insert(messageEntity);

        //保证最近的一次聊天记
        MessageUserRelation messageUserRelation = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelation>().and(e -> e.eq("send_id", messageDTO.getSendId()).eq("accept_id", messageDTO.getAcceptId())).orderByDesc("update_date"));

        if (messageUserRelation == null) {
            messageUserRelation = new MessageUserRelation();
            messageUserRelation.setContent(messageEntity.getContent());
            messageUserRelation.setSendId(messageEntity.getSendId());
            messageUserRelation.setAcceptId(messageEntity.getAcceptId());
            messageUserRelation.setCount(1);
            messageUserRelationDao.insert(messageUserRelation);
        } else {
            messageUserRelation.setContent(messageEntity.getContent());
            messageUserRelation.setCount(messageUserRelation.getCount() + 1);
            messageUserRelation.setUpdateDate(new Date());
            messageUserRelationDao.updateById(messageUserRelation);
        }

        MessageUserRelation messageUserRelationy2 = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelation>().and(e -> e.eq("send_id", messageDTO.getAcceptId()).eq("accept_id", messageDTO.getSendId())).orderByDesc("update_date"));
        if (messageUserRelationy2 == null) {
            messageUserRelationy2 = new MessageUserRelation();
            messageUserRelationy2.setContent(messageEntity.getContent());
            messageUserRelationy2.setSendId(messageEntity.getAcceptId());
            messageUserRelationy2.setAcceptId(messageEntity.getSendId());
            messageUserRelationDao.insert(messageUserRelationy2);
        } else {
            messageUserRelationy2.setContent(messageEntity.getContent());
            messageUserRelationy2.setUpdateDate(new Date());
            messageUserRelationDao.updateById(messageUserRelationy2);
        }

        try {
            List<MessageVo> chatUserList = getChatUserList(String.valueOf(messageUserRelation.getAcceptId()));
            WebSocketServer.sendMessageTo(JSON.toJSONString(chatUserList), String.valueOf(messageUserRelation.getAcceptId()));
        } catch (Exception e) {
            throw new YanHuoException(Constant.MSG_ERROR);
        }

    }

    @Override
    public List<MessageVo> getChatUserList(String uid) {

        List<MessageUserRelation> fromUserList = messageUserRelationDao.selectList(new QueryWrapper<MessageUserRelation>().eq("accept_id", uid));
        List<MessageVo> messageVoList = new ArrayList<>();

        List<Long> sendUids = fromUserList.stream().map(MessageUserRelation::getSendId).collect(Collectors.toList());
        List<User> userList = userDao.selectBatchIds(sendUids);
        HashMap<Long, User> userMap = new HashMap<>();

        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        for (MessageUserRelation model : fromUserList) {
            MessageVo messageVo = ConvertUtils.sourceToTarget(model, MessageVo.class);
            User userEntity = userMap.get(model.getSendId());
            messageVo.setSendId(String.valueOf(model.getSendId()))
                    .setAcceptId(String.valueOf(model.getAcceptId()))
                    .setTime(String.valueOf(model.getUpdateDate().getTime()))
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            messageVoList.add(messageVo);
        }
        return messageVoList;
    }

    @Override
    public Page<MessageVo> getChatRecord(long page, long limit, String sendUid, String acceptUid) {
        //得到所有的聊天记录
        List<Message> messageEntities = messageDao.selectList(new QueryWrapper<Message>().and(e -> e.eq("send_id", sendUid).eq("accept_id", acceptUid)));

        User sendUser = userDao.selectById(sendUid);
        List<MessageVo> messageVoList = new ArrayList<>();
        for (Message model : messageEntities) {
            MessageVo messageVo = new MessageVo();
            messageVo.setSendId(sendUid)
                    .setAcceptId(acceptUid)
                    .setContent(model.getContent())
                    .setTime(model.getTime())
                    .setDate(DateUtils.format(model.getCreateDate(), DATE_TIME_PATTERN))
                    .setUsername(sendUser.getUsername())
                    .setAvatar(sendUser.getAvatar());
            messageVoList.add(messageVo);
        }

        List<Message> messageEntities2 = messageDao.selectList(new QueryWrapper<Message>().and(e -> e.eq("send_id", acceptUid).eq("accept_id", sendUid)));

        User acceptUser = userDao.selectById(acceptUid);
        List<MessageVo> messageVoList2 = new ArrayList<>();
        for (Message model : messageEntities2) {

            MessageVo messageVo = new MessageVo();
            messageVo.setAcceptId(sendUid)
                    .setSendId(acceptUid)
                    .setContent(model.getContent())
                    .setTime(model.getTime())
                    .setDate(DateUtils.format(model.getCreateDate(), DATE_TIME_PATTERN))
                    .setUsername(acceptUser.getUsername())
                    .setAvatar(acceptUser.getAvatar());
            messageVoList2.add(messageVo);
        }
        messageVoList.addAll(messageVoList2);
        messageVoList.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));

        return PageUtils.getPages((int) page, (int) limit, messageVoList);
    }

    @Override
    public void updateRecordCount(String sendId, String acceptId) {
        MessageUserRelation messageUserRelation = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelation>().and(e -> e.eq("send_id", sendId).eq("accept_id", acceptId)));
        messageUserRelation.setCount(0);
        messageUserRelationDao.updateById(messageUserRelation);
    }

    @Override
    public void deleteRecord(String sendId, String acceptId) {
        messageUserRelationDao.delete(new QueryWrapper<MessageUserRelation>().and(e -> e.eq("send_id", sendId).eq("accept_id", acceptId)));
    }
}
