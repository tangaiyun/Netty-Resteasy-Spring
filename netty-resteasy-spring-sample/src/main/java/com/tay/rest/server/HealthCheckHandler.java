package com.tay.rest.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class HealthCheckHandler extends IdleStateAwareChannelHandler{
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		 super.channelIdle(ctx, e);  

		 if(e.getState() == IdleState.ALL_IDLE) {
			 e.getChannel().close();
			 System.out.println("idle channel was closed!");
		 }
    }
}
