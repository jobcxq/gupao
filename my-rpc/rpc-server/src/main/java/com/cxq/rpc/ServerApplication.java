package com.cxq.rpc;

import com.cxq.rpc.server.RpcServer;
import com.cxq.rpc.service.UserService;
import com.cxq.rpc.service.impl.UserServiceImpl;

/**
 * Hello world!
 */
public class ServerApplication {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        RpcServer server = new RpcServer(userService,8080);
        server.acceptMessage();
    }
}
