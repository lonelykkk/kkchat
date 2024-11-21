package com.kkk.websocket;

import com.kkk.entity.dto.MessageSendDto;
import com.kkk.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消息处理器
 *
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/21 7:55
 * @Version V1.0
 */
@Component("messageHandler")
@Slf4j
public class MessageHandler {
    private static final String MESSAGE_TOPIC = "message.topic";
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ChannelContextUtils channelContextUtils;

    @PostConstruct
    public void lisMessage() {
        RTopic rTopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.addListener(MessageSendDto.class, (MessageSendDto, sendDto) -> {
            log.info("收到广播消息：{}", JsonUtils.convertObj2Json(sendDto));
            //channelContextUtils.sendMsg(sendDto,)
        });
    }
    public void sendMessage(MessageSendDto sendDto) {
        RTopic rTopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.publish(sendDto);
    }
}
