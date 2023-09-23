package com.yanhuo.platform.listener;

import com.rabbitmq.client.Channel;
import com.yanhuo.common.constant.platform.PlatformMqConstant;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PlatformMqListener {

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    AlbumService albumService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = PlatformMqConstant.IMG_DETAIL_STATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = PlatformMqConstant.IMG_DETAIL_STATE_EXCHANGE),
            key = {PlatformMqConstant.IMG_DETAIL_STATE_KEY}
    ))
    public void imgDetailStateListener(ImgDetail imgDetail, Message message, Channel channel) throws IOException {
        imgDetailService.updateById(imgDetail);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = PlatformMqConstant.USER_STATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = PlatformMqConstant.USER_STATE_EXCHANGE),
            key = {PlatformMqConstant.USER_STATE_KEY}
    ))
    public void userStateListener(User user, Message message, Channel channel) throws IOException {
        userService.updateById(user);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = PlatformMqConstant.COMMON_STATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = PlatformMqConstant.COMMON_STATE_EXCHANGE),
            key = {PlatformMqConstant.COMMON_STATE_KEY}
    ))
    public void commentStateListener(Comment comment, Message message, Channel channel) throws IOException {
        commentService.updateById(comment);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = PlatformMqConstant.ALBUM_STATE_QUEUE, durable = "true"),
            exchange = @Exchange(value = PlatformMqConstant.ALBUM_STATE_EXCHANGE),
            key = {PlatformMqConstant.ALBUM_STATE_KEY}
    ))
    public void albumStateListener(Album album, Message message, Channel channel) throws IOException {
        albumService.updateById(album);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
