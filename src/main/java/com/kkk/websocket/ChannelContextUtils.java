package com.kkk.websocket;

import com.kkk.redis.RedisComponent;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/19 15:08
 * @Version V1.0
 */
@Component
@Slf4j
public class ChannelContextUtils {

    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTEXT_MAP = new ConcurrentHashMap<>();
    @Resource
    private RedisComponent redisComponent;

    /**
     * 这段代码执行后可以通过channelId获取用户id
     * @param userId 用户id
     * @param channel 当前连接的通道id
     */
    public void addContext(String userId, Channel channel) {
        String channelId = channel.id().toString();
        log.info("channelId->{}", channelId);
        AttributeKey attributeKey = null;
        if (!AttributeKey.exists(channelId)) {
            attributeKey = AttributeKey.newInstance(channelId);
        } else {
            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);

        USER_CONTEXT_MAP.put(userId, channel);
        redisComponent.saveUserHeartBeat(userId);

        String groupId = "10000";
        add2Group(groupId, channel);
    }

    private void add2Group(String groupId, Channel channel) {
        ChannelGroup group = GROUP_CONTEXT_MAP.get(groupId);
        if (group == null) {
            group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTEXT_MAP.put(groupId, group);
        }
        if (channel == null) {
            return;
        }
        group.add(channel);
    }

    public void send2Group(String message) {
        ChannelGroup group = GROUP_CONTEXT_MAP.get("10000");
        group.writeAndFlush(new TextWebSocketFrame(message));

    }
}
