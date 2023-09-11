package com.yanhuo.platform.listener;

import com.rabbitmq.client.Channel;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.xo.dto.platform.ImgDetailDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 暂时用不到mq
 */
//@Component
public class MqListener {

    @Autowired
    ImgDetailService imgDetailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yanhuo.upload.queue", durable = "true"),
            exchange = @Exchange(value = "yanhuo.upload.direct"),
            key = {"yanhuo.upload"}
    ))
    public void ImgDetailListener(ImgDetailDTO imgDetailDTO, Message message, Channel channel) throws IOException {
        if (imgDetailDTO.getType() == 0) {
            //上传任务
            imgDetailService.publish(imgDetailDTO);
        } else {
            //修改任务

        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
