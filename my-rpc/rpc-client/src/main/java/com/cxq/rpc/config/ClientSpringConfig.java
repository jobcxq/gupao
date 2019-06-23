package com.cxq.rpc.config;

import com.cxq.rpc.processor.RpcProxyClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cnxqin
 * @desc 使用 Spring 来管理
 * @date 2019/06/24 00:08
 */
@Configuration
public class ClientSpringConfig {

    @Bean("rpcProxyClient")
    public RpcProxyClient rpcProxyClient(){
        return new RpcProxyClient("localhost",8080);
    }

}
