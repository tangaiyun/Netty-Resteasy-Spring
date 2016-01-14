package com.tay.rest.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tay.rest.auth.DefaultUserService;
import com.tay.rest.auth.User;

@Component
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_PROPERTY = "UserToken";
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());;
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());;
    protected final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        //Access allowed for all
        if( ! method.isAnnotationPresent(PermitAll.class))
        {
            //Access denied for all
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
             
            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
             
            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
             
            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
             
            //Get user token
            final String userToken = authorization.get(0);
            
            DefaultUserService userService = DefaultUserService.getInstance();
            
            User user = userService.getUser(userToken); 
            
            if(user == null) {
            	requestContext.abortWith(ACCESS_DENIED);
            	return;
            }
           
            //Verifying Username and password
            System.out.println(user.getUserName());
            System.out.println(user.getPassword());
            
             
            //Verify user access
            if(method.isAnnotationPresent(RolesAllowed.class))
            {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
                 
                //Is user valid?
                if(!rolesSet.contains(user.getRole().name()))
                {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
	}
}
