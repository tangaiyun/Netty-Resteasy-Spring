package com.tay.rest.server;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

@Component
@Provider
public class MyExceptionHandler implements ExceptionMapper<BizException>{

	public Response toResponse(BizException exception) {
		return Response.status(Status.BAD_REQUEST).entity(exception).build();
	}

}
