package com.kkk.websocket.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/18 18:53
 * @Version V1.0
 */
public class HandlerHeartBeat extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(HandlerHeartBeat.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.info("心跳超时");
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("heart");
            }
        }
    }
}
