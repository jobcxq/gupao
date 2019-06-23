package com.cxq.rpc;

import com.cxq.rpc.model.User;
import com.cxq.rpc.processor.RpcProxyClient;
import com.cxq.rpc.service.UserService;

/**
 * Hello world!
 */
public class ClientApplication {
    public static void main(String[] args) {

        UserService userService = new RpcProxyClient("localhost",8080)
                .createClientProxy(UserService.class);
        String result = userService.sayHello("cxq");
        System.out.println(result);

//        User user = new User();
//        user.setName("cxq");
//        user.setAge(18);
//        System.out.println(userService.addUser(user));

    }
}
