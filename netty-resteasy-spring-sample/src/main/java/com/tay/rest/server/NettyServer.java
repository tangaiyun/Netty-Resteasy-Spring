package com.tay.rest.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.ws.rs.ext.Provider;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

@Component
public class NettyServer {

	@Autowired
	ApplicationContext				ac;

	String							rootResourcePath	= "/resteasy";
	int								port				= 8082;
	private final int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
	private final int executorThreadCount = Runtime.getRuntime().availableProcessors() * 4;
	private SSLContext sslContext = null;
	private final int maxRequestSize = 1024 * 1024 * 10;
	private final ChannelHandler channelHandler = new MyChannelHandler();
	NettyJaxrsServer	netty;

	public void start() {

		ResteasyDeployment dp = new ResteasyDeployment();

		Collection<Object> providers = ac.getBeansWithAnnotation(Provider.class).values();
		Collection<Object> controllers = ac.getBeansWithAnnotation(Controller.class).values();

		Assert.notEmpty(controllers);

		// extract providers
		if (providers != null) {
			dp.getProviders().addAll(providers);
		}
		// extract only controller annotated beans
		dp.getResources().addAll(controllers);
		Map<String, Object> channelOptions = new HashMap<String, Object>();
		channelOptions.put("reuseAddress", true);
		List<ChannelHandler> channelHandlerList = Collections.singletonList(channelHandler);
		netty = new NettyJaxrsServer();
		netty.setChannelOptions(channelOptions);
		netty.setDeployment(dp);
		netty.setPort(port);
		netty.setRootResourcePath("/resteasy");
		netty.setIoWorkerCount(ioWorkerCount);
		netty.setExecutorThreadCount(executorThreadCount);
		netty.setMaxRequestSize(maxRequestSize);
		netty.setSSLContext(sslContext);
		netty.setKeepAlive(false);
		netty.setChannelHandlers(channelHandlerList);
		netty.setSecurityDomain(null);
		netty.start();
	}

	@PreDestroy
	public void cleanUp() {
		netty.stop();
	}

	public String getRootResourcePath() {
		return rootResourcePath;
	}

	public int getPort() {
		return port;
	}

}
