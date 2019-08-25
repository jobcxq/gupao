package com.cxq.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author cnxqin
 * @desc
 * @date 2019/08/25 14:08
 */
public class ZKRegistryCenter {

    private static final String CONNECT_STRING = "CentOS_01:2181";

    private static CuratorFramework curatorFramework;

    private ZKRegistryCenter(){}

    static{
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))  //重试策略
                .namespace("registry").build();
        curatorFramework.start();   //启动
        System.out.println("Zookeeper is connected...\n");
        ZKRegistryCenter.curatorFramework = curatorFramework;
    }


    public static void register(String serviceName, String serviceAddress){
        //服务注册
        try {
            String servicePath = "/" + serviceName + "/" + serviceAddress;
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).
                    forPath(servicePath);
            System.out.println("服务：[" + serviceName + "],注册成功，地址：[" + serviceAddress + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
