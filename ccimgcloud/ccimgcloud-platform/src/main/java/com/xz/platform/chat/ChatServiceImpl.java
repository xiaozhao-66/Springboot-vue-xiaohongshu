package com.xz.platform.chat;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.exception.RenException;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.MessageDao;
import com.xz.platform.dao.MessageUserRelationDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.dao.UserRecordDao;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.entity.MessageEntity;
import com.xz.platform.entity.MessageUserRelationEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.entity.UserRecordEntity;
import com.xz.platform.vo.MessageVo;
import com.xz.platform.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao
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

    @Autowired
    UserRecordDao userRecordDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addChatRecord(MessageDTO messageDTO) {
        //往数据库里面添加记录，
        MessageEntity messageEntity = ConvertUtils.sourceToTarget(messageDTO, MessageEntity.class);
        messageEntity.setTime(String.valueOf(System.currentTimeMillis()));
        messageDao.insert(messageEntity);

        //保证最近的一次聊天记
        MessageUserRelationEntity messageUserRelationEntity = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelationEntity>().and(e -> e.eq("send_id", messageDTO.getSendId()).eq("accept_id", messageDTO.getAcceptId())).orderByDesc("update_date"));

        if (messageUserRelationEntity == null) {
            messageUserRelationEntity = new MessageUserRelationEntity();
            messageUserRelationEntity.setContent(messageEntity.getContent());
            messageUserRelationEntity.setSendId(messageEntity.getSendId());
            messageUserRelationEntity.setAcceptId(messageEntity.getAcceptId());
            messageUserRelationEntity.setCount(1);
            messageUserRelationDao.insert(messageUserRelationEntity);
        } else {
            messageUserRelationEntity.setContent(messageEntity.getContent());
            messageUserRelationEntity.setCount(messageUserRelationEntity.getCount() + 1);
            messageUserRelationEntity.setUpdateDate(new Date());
            messageUserRelationDao.updateById(messageUserRelationEntity);
        }

        MessageUserRelationEntity messageUserRelationEntity2 = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelationEntity>().and(e -> e.eq("send_id", messageDTO.getAcceptId()).eq("accept_id", messageDTO.getSendId())).orderByDesc("update_date"));
        if (messageUserRelationEntity2 == null) {
            messageUserRelationEntity2 = new MessageUserRelationEntity();
            messageUserRelationEntity2.setContent(messageEntity.getContent());
            messageUserRelationEntity2.setSendId(messageEntity.getAcceptId());
            messageUserRelationEntity2.setAcceptId(messageEntity.getSendId());
            messageUserRelationDao.insert(messageUserRelationEntity2);
        } else {
            messageUserRelationEntity2.setContent(messageEntity.getContent());
            messageUserRelationEntity2.setUpdateDate(new Date());
            messageUserRelationDao.updateById(messageUserRelationEntity2);
        }

        try {
            List<MessageVo> chatUserList = getChatUserList(String.valueOf(messageUserRelationEntity.getAcceptId()));
            WebSocketServer.sendMessageTo(JSON.toJSONString(chatUserList), String.valueOf(messageUserRelationEntity.getAcceptId()));
        } catch (Exception e) {
            throw new RenException(Constant.MSG_ERROR);
        }

    }

    @Override
    public List<MessageVo> getChatUserList(String uid) {

        List<MessageUserRelationEntity> fromUserList = messageUserRelationDao.selectList(new QueryWrapper<MessageUserRelationEntity>().eq("accept_id", uid));
        List<MessageVo> messageVoList = new ArrayList<>();

        List<Long> sendUids = fromUserList.stream().map(MessageUserRelationEntity::getSendId).collect(Collectors.toList());
        List<UserEntity> userList = userDao.selectBatchIds(sendUids);
        HashMap<Long, UserEntity> userMap = new HashMap<>();

        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });


        for (MessageUserRelationEntity model : fromUserList) {
            MessageVo messageVo = ConvertUtils.sourceToTarget(model, MessageVo.class);
            UserEntity userEntity = userMap.get(model.getSendId());
            messageVo.setSendId(String.valueOf(model.getSendId()))
                    .setAcceptId(String.valueOf(model.getAcceptId()))
                    .setDate(DateUtils.timeUtile(model.getUpdateDate()))
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            messageVoList.add(messageVo);
        }
        return messageVoList;
    }

    @Override
    public Page<MessageVo> getChatRecord(long page, long limit, String sendUid, String acceptUid) {
        //得到所有的聊天记录
        List<MessageEntity> messageEntities = messageDao.selectList(new QueryWrapper<MessageEntity>().and(e -> e.eq("send_id", sendUid).eq("accept_id", acceptUid)));

        UserEntity sendUser = userDao.selectById(sendUid);
        List<MessageVo> messageVoList = new ArrayList<>();
        for (MessageEntity model : messageEntities) {
            MessageVo messageVo = new MessageVo();
            messageVo.setSendId(sendUid)
                    .setAcceptId(acceptUid)
                    .setContent(model.getContent())
                    .setTime(model.getTime())
                    .setDate(DateUtils.timeUtile(model.getCreateDate()))
                    .setUsername(sendUser.getUsername())
                    .setAvatar(sendUser.getAvatar());
            messageVoList.add(messageVo);
        }

        List<MessageEntity> messageEntities2 = messageDao.selectList(new QueryWrapper<MessageEntity>().and(e -> e.eq("send_id", acceptUid).eq("accept_id", sendUid)));

        UserEntity acceptUser = userDao.selectById(acceptUid);
        List<MessageVo> messageVoList2 = new ArrayList<>();
        for (MessageEntity model : messageEntities2) {

            MessageVo messageVo = new MessageVo();
            messageVo.setAcceptId(sendUid)
                    .setSendId(acceptUid)
                    .setContent(model.getContent())
                    .setTime(model.getTime())
                    .setDate(DateUtils.timeUtile(model.getCreateDate()))
                    .setUsername(acceptUser.getUsername())
                    .setAvatar(acceptUser.getAvatar());
            messageVoList2.add(messageVo);
        }
        messageVoList.addAll(messageVoList2);
        messageVoList.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));

        return PageUtils.getPages((int) page, (int) limit, messageVoList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRecordCount(String sendId, String acceptId) {
        MessageUserRelationEntity messageUserRelationEntity = messageUserRelationDao.selectOne(new QueryWrapper<MessageUserRelationEntity>().and(e -> e.eq("send_id", sendId).eq("accept_id", acceptId)));

        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", acceptId));
        userRecordEntity.setNochatCount(userRecordEntity.getNochatCount() - messageUserRelationEntity.getCount());
        userRecordDao.updateById(userRecordEntity);

        messageUserRelationEntity.setCount(0);
        messageUserRelationDao.updateById(messageUserRelationEntity);
    }

    @Override
    public void deleteRecord(String sendId, String acceptId) {
        messageUserRelationDao.delete(new QueryWrapper<MessageUserRelationEntity>().and(e -> e.eq("send_id", sendId).eq("accept_id", acceptId)));
    }
}
