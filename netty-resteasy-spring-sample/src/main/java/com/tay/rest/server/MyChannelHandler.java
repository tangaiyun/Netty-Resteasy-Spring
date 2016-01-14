package com.tay.rest.server;


import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyChannelHandler extends SimpleChannelHandler {
	private final static Logger logger = LoggerFactory.getLogger(MyChannelHandler.class);
	@Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        // Log all channel state changes.
        if (e instanceof ChannelStateEvent) {
            logger.info("Channel state changed: " + e);
        }

        super.handleUpstream(ctx, e);
    }

	@Override
	public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		System.out.println("caught a error."+e.getCause());
	}
}
