package com.cxq.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author cnxqin
 * @desc
 * @date 2019/08/21 22:33
 */
public class ZKConnection {

    private static final String CONNECT_STRING = "CentOS_01:2181";

    public static CuratorFramework getConnection(String namespace){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))  //重试策略
                .namespace(namespace).build();
        /** retryPolicy - 重试策略
         * Curator 内部实现的几种重试策略:
         * ExponentialBackoffRetry:重试指定的次数, 且每一次重试之间停顿的时间逐渐增加
         * RetryNTimes:指定最大重试次数的重试策略
         * RetryOneTime:仅重试一次
         * RetryUntilElapsed:一直重试直到达到规定的时间
         *
         * namespace:客户端对 Zookeeper 上数据节点的任何操作都是相对/namespace
         *             目录进行的，这有利于实现不同的 Zookeeper 的业务之间的隔离
         *
         */

        curatorFramework.start();   //启动
        System.out.println("Zookeeper is started...\n");
        return curatorFramework;
    }

    private ZKConnection (){
    }

}
