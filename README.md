# Netty-Resteasy-Spring
a template project for Restful API development based on Netty, Resteasy and Spring
=====================

Resteasy + Spring + Netty sample

* Inject resteasy provider / controllers as spring bean
* Authentication based on javax.ws.rs.container.ContainerRequestFilter
* custom exception handler
* custom ChannelHandler
* add idle connections check and close
* add API anti-defacement depends on request signature and response signature 
* add API throughput control
* add API anti-replay 
* change dependencies to update version

=====================

1. Run at Main.java
2. Test http://localhost:8082/resteasy/hello/world
3. more test cases please check the com.tay.rest.controller.testHomeController
4. please check all test cases to know how to utilize these new features

=====================

run 'mvn clean package' to generate netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar and original-netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar
the netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar is a runnable archive.
command : java -jar netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar


contact: 
qq: 15520929
wechat: zhuiyingderen
email: aiyun.tang@gmail.com
