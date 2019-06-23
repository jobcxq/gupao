package com.cxq.rpc.processor;

import com.cxq.rpc.util.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author cnxqin
 * @desc
 * @date 2019/06/23 00:16
 */
public class RpcProxyClient {

    private String ip;
    private int port;

    public RpcProxyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> T createClientProxy(final Class<T> interfaceClazz){
        System.out.println("createClientProxy : " + interfaceClazz.getName());
        return (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class<?>[]{interfaceClazz},new RemoteInvocationHandler(ip, port));
    }

    /**
     * 代理类
     */
    private class RemoteInvocationHandler implements InvocationHandler{

        private String ip;
        private int port;

        public RemoteInvocationHandler(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("client RemoteInvocationHandler.invoke : " + method.getName());
            //组装请求对象
            RpcRequest request = new RpcRequest();
            request.setServiceName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            request.setArgs(args);

            RpcClientProcessor processor = new RpcClientProcessor(ip, port);
            return processor.sendMessage(request);
        }
    }

}
