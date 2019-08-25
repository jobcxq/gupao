package com.cxq.rpc;

import com.cxq.rpc.config.ClientSpringConfig;
import com.cxq.rpc.model.User;
import com.cxq.rpc.processor.RpcProxyClient;
import com.cxq.rpc.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 */
public class ClientApplication {
    public static void main(String[] args) {

//        UserService userService = new RpcProxyClient("localhost",8080)
//                .createClientProxy(UserService.class);

        ApplicationContext context = new AnnotationConfigApplicationContext(ClientSpringConfig.class);
        RpcProxyClient rpcProxyClient = (RpcProxyClient) context.getBean("rpcProxyClient");
        UserService userService  = rpcProxyClient.createClientProxy(UserService.class);

        //测试ZK中客户端服务的上下线
        for(int i = 0 ; i < 1000; i ++){
            String result = userService.sayHello("cxq");
            System.out.println(result);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        User user = new User();
//        user.setName("cxq");
//        user.setAge(18);
//        System.out.println(userService.addUser(user));

    }
}
