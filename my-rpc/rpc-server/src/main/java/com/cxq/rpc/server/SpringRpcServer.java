package com.cxq.rpc.server;


import com.cxq.rpc.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cnxqin
 * @desc 使用 SpringRpcServer 替换 RpcServer ，来集成Spring
 * @date 2019/06/23 23:45
 */
public class SpringRpcServer implements ApplicationContextAware, InitializingBean {

    private Map<String,Object> rpcServiceMap = new HashMap();

    ExecutorService executorService= Executors.newCachedThreadPool();

    private int port;

    public SpringRpcServer(int port) {
        this.port = port;
    }

    /**
     * 初始化所有的 rpc 服务
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //所有添加了 RpcService 的类（rpc服务类）
        Map<String,Object> beanMap = applicationContext.getBeansWithAnnotation(RpcService.class);

        if(beanMap != null){
            for(Object servcieBean : beanMap.values()){
                RpcService rpcService = servcieBean.getClass().getAnnotation((RpcService.class));
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if(!StringUtils.isEmpty(version)){
                    serviceName += "-" + version;
                }
                rpcServiceMap.put(serviceName,servcieBean);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            while (true){
                Socket socket = serverSocket.accept();  //BIO
                //每一个socket 交给一个processorHandler来处理
                executorService.submit(new RpcServerProcessor(socket, rpcServiceMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != serverSocket){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
