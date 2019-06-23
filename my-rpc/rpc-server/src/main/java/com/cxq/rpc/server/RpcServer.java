package com.cxq.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cnxqin
 * @desc 服务端接收请求主类
 * @date 2019/06/23 00:37
 */
public class RpcServer {

    ExecutorService executorService= Executors.newCachedThreadPool();

    private Object service;
    private int port;

    public RpcServer(Object service, int port) {
        this.service = service;
        this.port = port;
    }

    public void acceptMessage(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            while (true){
                Socket socket = serverSocket.accept();  //BIO
                //每一个socket 交给一个processorHandler来处理
                System.out.println("accept...");
                executorService.submit(new RpcServerProcessor(service,socket));
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
