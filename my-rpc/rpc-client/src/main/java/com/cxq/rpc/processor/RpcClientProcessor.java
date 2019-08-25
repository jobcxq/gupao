package com.cxq.rpc.processor;

import com.cxq.rpc.discovery.ZKServerDiscovery;
import com.cxq.rpc.util.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author cnxqin
 * @desc 客户端真正发送请求的类
 * @date 2019/06/23 00:25
 */
public class RpcClientProcessor {

    private String ip;
    private int port;

    public RpcClientProcessor(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 建立 socket 连接发送请求
     * @param request 请求数据
     * @return 返回服务器返回的数据
     */
    public Object sendMessage(RpcRequest request) {
        System.out.println("client sendMessage");
        Object result = null;
        Socket socket = null;

        ObjectOutputStream os = null;
        ObjectInputStream is = null;
        try {
//            socket = new Socket(ip,port);   //建立连接
            //建立连接，使用ZK做服务注册中心
            String[] address = ZKServerDiscovery.discovery(request.getServiceName()).split(":");
            socket = new Socket(address[0], Integer.parseInt(address[1]));

            os = new ObjectOutputStream(socket.getOutputStream());  //网络socket
            os.writeObject(request);    //序列化()
            os.flush();

            is = new ObjectInputStream(socket.getInputStream());
            result  = is.readObject();
            System.out.println("client receieve message : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
