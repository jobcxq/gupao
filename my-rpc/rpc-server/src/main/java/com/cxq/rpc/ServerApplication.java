package com.cxq.rpc;

import com.cxq.rpc.config.ServerSpringConfig;
import com.cxq.rpc.server.RpcServer;
import com.cxq.rpc.service.UserService;
import com.cxq.rpc.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 */
public class ServerApplication {
    public static void main(String[] args) {

//        UserService userService = new UserServiceImpl();
//        RpcServer server = new RpcServer(userService,8080);
//        server.acceptMessage();

        ApplicationContext context = new AnnotationConfigApplicationContext(ServerSpringConfig.class);
        ((AnnotationConfigApplicationContext) context).start();
    }
}
