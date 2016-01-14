resteasy-spring-netty
=====================

Resteasy + Spring + Netty sample

* Inject resteasy provider / controllers as spring bean
* Authentication based on javax.ws.rs.container.ContainerRequestFilter
* custom exception handler
* custom ChannelHandler
* change dependencies to update version

=====================

1. Run at Main.java
2. Test http://localhost:8082/resteasy/hello/world
3. more test cases please check the com.tay.rest.controller.testHomeController

=====================

run 'mvn clean package' to generate netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar and original-netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar
the netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar is a runnable archive.
command : java -jar netty-resteasy-spring-sample-0.0.1-SNAPSHOT.jar

