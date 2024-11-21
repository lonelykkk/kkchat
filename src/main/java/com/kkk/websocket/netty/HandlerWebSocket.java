package com.kkk.websocket.netty;

import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.redis.RedisComponent;
import com.kkk.utils.StringTools;
import com.kkk.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/18 19:02
 * @Version V1.0
 */
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private ChannelContextUtils channelContextUtils;

    /**
     * 通道就绪后调用，一般用来做初始化
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Channel channel = ctx.channel();
        logger.info("有新的连接加入。。。");
    }

    /**
     * 当通道不再活跃时（连接关闭）会调用此方法，我们可以在这里做一些清理工作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接已经断开。。。");
        channelContextUtils.removeContext(ctx.channel());
    }

    /**
     * 读就绪事件 当有消息可读时会调用此方法，我们可以在这里读取消息并处理。
     *
     * @param ctx
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //接收心跳
        Channel channel = ctx.channel();
        //logger.info("收到消息{}", textWebSocketFrame.text());
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        //logger.info("收到userId->{}的消息:{}", userId, textWebSocketFrame.text());
        redisComponent.saveUserHeartBeat(userId);

//        channelContextUtils.send2Group(textWebSocketFrame.text());

    }

    //用于处理用户自定义的事件  当有用户事件触发时会调用此方法，例如连接超时，异常等。
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = complete.requestUri();
            String token = getToken(url);
            if (token == null) {
                ctx.channel().close();
                return;
            }
            TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDto(token);
            if (null == tokenUserInfoDto) {
                ctx.channel().close();
                return;
            }
            /**
             * 用户加入
             */
            channelContextUtils.addContext(tokenUserInfoDto.getUserId(), ctx.channel());

        }
    }

    private String getToken(String url) {
        if (StringTools.isEmpty(url) || url.indexOf("?") == -1) {
            return null;
        }
        String[] queryParams = url.split("\\?");
        if (queryParams.length != 2) {
            return null;
        }
        String[] params = queryParams[1].split("=");
        if (params.length != 2) {
            return null;
        }
        return params[1];
    }
}
