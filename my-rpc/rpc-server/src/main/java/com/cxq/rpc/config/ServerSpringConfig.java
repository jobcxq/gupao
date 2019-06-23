package com.cxq.rpc.config;

import com.cxq.rpc.server.SpringRpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author cnxqin
 * @desc
 * @date 2019/06/24 00:04
 */

@Configuration
@ComponentScan(basePackages = "com.cxq.rpc")
public class ServerSpringConfig {

    @Bean(name="springRpcServer")
    public SpringRpcServer rpcServer(){
        return new SpringRpcServer(8080);
    }

}
