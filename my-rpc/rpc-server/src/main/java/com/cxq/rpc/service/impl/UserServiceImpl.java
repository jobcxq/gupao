package com.cxq.rpc.service.impl;

import com.cxq.rpc.annotation.RpcService;
import com.cxq.rpc.model.User;
import com.cxq.rpc.service.UserService;

/**
 * @author cnxqin
 * @desc
 * @date 2019/06/23 00:02
 */
@RpcService(UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String name) {
        System.out.println("hello " + name);
        return "hello " + name + "!";
    }

    @Override
    public String addUser(User user) {
        System.out.println("add user : " + user.toString());
        return "success";
    }
}
