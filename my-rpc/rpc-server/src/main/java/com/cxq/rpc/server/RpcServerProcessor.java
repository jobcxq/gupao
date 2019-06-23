package com.cxq.rpc.server;

import com.cxq.rpc.util.RpcRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author cnxqin
 * @desc 处理服务器请求
 * @date 2019/06/23 00:41
 */
public class RpcServerProcessor implements Runnable{

    private Object service;
    private Socket socket;

    /**
     * 使用 Spring 管理时使用
     */
    private Map<String, Object> rpcServiceMap;

    public RpcServerProcessor(Object service, Socket socket) {
        this.service = service;
        this.socket = socket;
    }

    public RpcServerProcessor(Socket socket, Map<String, Object> rpcServiceMap) {
        this.socket = socket;
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    public void run() {
        ObjectInputStream is = null;
        ObjectOutputStream os = null;

        try {
            is = new ObjectInputStream(socket.getInputStream());
            RpcRequest request = (RpcRequest) is.readObject();
            Object result = this.invoke(request);

            os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(result);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest request) throws Exception {

        Object service = null;

        //使用 Spring 管理时使用
        if(rpcServiceMap != null){
            String serviceName = request.getServiceName();
            String version = request.getVersion();
            //增加版本号的判断
            if(!StringUtils.isEmpty(version)){
                serviceName += "-" + version;
            }

            service = rpcServiceMap.get(serviceName);   //从 Spring 初始化时的 Map 中获取服务
            if(service==null){
                throw new RuntimeException("service not found:" + serviceName);
            }
        }else{
            service = this.service; //使用传过来的服务
        }

        //处理参数类型
        Class<?>[] types = new Class[request.getArgs() != null ? request.getArgs().length : 0];
        for(int i = 0; i < types.length; i ++){
            types[i] = request.getArgs()[i].getClass();
        }

        Class<?> clazz = Class.forName(request.getServiceName());
        Method method = clazz.getDeclaredMethod(request.getMethodName(),types);

        return method.invoke(service, request.getArgs());
    }
}
