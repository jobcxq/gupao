package com.cxq.rpc.service;

import com.cxq.rpc.model.User;

/**
 * @author cnxqin
 * @desc rpc接口定义
 * @date 2019/06/22 23:35
 */
public interface UserService {

    String sayHello(String name);

    String addUser(User user);


}
