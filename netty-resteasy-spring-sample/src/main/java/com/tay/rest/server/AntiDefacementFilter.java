package com.tay.rest.server;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tay.rest.antidefacement.AntiDefacement;
import com.tay.rest.antidefacement.Coder;
import com.tay.rest.antidefacement.DuplicateRequestChecker;

@Component
@Provider
public class AntiDefacementFilter implements ContainerRequestFilter {
	public static final String ACCESS_RANDOM_STR = "ACCESS_RANDOM_STR";
	public static final int ACCESS_RANDOM_STR_LENGTH = 8;
	public static final String ACCESS_TIME = "ACCESS_TIME";
	public static final String ACCESS_SIGNATURE = "ACCESS_SIGNATURE";
	private static final ServerResponse BAD_REQUEST = new ServerResponse("Invalid header attributes.", 403,
			new Headers<Object>());;
	private static final ServerResponse DUPLICATE_REQUEST = new ServerResponse("Dupliucate request.", 403,
			new Headers<Object>());;
	@Autowired
	private DuplicateRequestChecker duplicateRequestChecker;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
				.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		Method method = methodInvoker.getMethod();
		// 接口需要验证数据是否篡改
		if (method.isAnnotationPresent(AntiDefacement.class)) {
			// Get request headers
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			// Fetch access random String and access time from header
			final String accessRandomStr = headers.getFirst(ACCESS_RANDOM_STR);
			final String accessTime = headers.getFirst(ACCESS_TIME);
			final String accessSignature = headers.getFirst(ACCESS_SIGNATURE);
			if (StringUtils.isEmpty(accessRandomStr) || StringUtils.length(accessRandomStr) != ACCESS_RANDOM_STR_LENGTH
					|| StringUtils.isEmpty(accessTime) || !StringUtils.isNumeric(accessTime)
					|| StringUtils.isEmpty(accessSignature)) {
				requestContext.abortWith(BAD_REQUEST);
				return;
			}
			ChannelBufferInputStream cbis = (ChannelBufferInputStream) requestContext.getEntityStream();
			int length = cbis.available();
			byte[] bytes = new byte[length];
			cbis.readFully(bytes);
			cbis.reset();
			String requestBody = new String(bytes, "utf-8");
			String localSignature = Coder.genSignature(requestBody);
			if (!StringUtils.equals(accessSignature, localSignature)) {
				requestContext.abortWith(BAD_REQUEST);
				return;
			}
			String requestUniqueKey = (new StringBuilder()).append(accessRandomStr).append(accessTime)
					.append(accessSignature).toString();

			if (duplicateRequestChecker.checkDuplicate(requestUniqueKey)) {
				requestContext.abortWith(DUPLICATE_REQUEST);
				return;
			}
		}
	}
}
