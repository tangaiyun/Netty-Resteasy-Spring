package com.tay.rest.controller;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import com.tay.rest.auth.DefaultUserService;
import com.tay.rest.auth.UserLogin;
import com.tay.rest.pojo.Article;
import com.tay.rest.pojo.Helloworld;
import com.tay.rest.server.BizException;

@Controller
@Path("/hello")
public class HomeController {
	// 允许所有访问
	@PermitAll
	@GET
	@Path("/world")
	@Produces("application/json")
	public Response helloworld() throws Exception {
		Helloworld hw = new Helloworld("Welcome, HelloWorld");
		return Response.status(200).entity(hw).build();
	}

	// 允许所有访问
	@PermitAll
	@GET
	@Path("/error")
	@Produces("application/json")
	public Response error() throws Exception {
		throw new BizException("TEST", "test exception");
	}

	// 允许所有访问
	@PermitAll
	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_PLAIN })
	public Response login(UserLogin userLogin) {
		String userToken = DefaultUserService.getInstance().login(userLogin);
		if (userToken != null) {
			return Response.status(200).entity(userToken).build();
		} else {
			return Response.status(406).entity("login failed").build();
		}
	}

	// 允许普通用户和主力用户访问访问
	@RolesAllowed({ "USER", "MAIN_FORCES" })
	@POST
	@Path("/singlesave")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response save(Article article) {
		return Response.status(200).entity(article).build();
	}

	@RolesAllowed("MAIN_FORCES")
	@POST
	@Path("/multisave")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Article> save(@QueryParam("multi") boolean isMulti, List<Article> articles) {
		return articles;
	}
}
