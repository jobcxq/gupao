package com.cxq.rpc.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cnxqin
 * @desc
 * @date 2019/08/25 14:33
 */
public class ZKServerDiscovery {

    private static final String CONNECT_STRING = "CentOS_01:2181";

    private static CuratorFramework curatorFramework;

    private ZKServerDiscovery() {
    }

    private static List<String> serviceAddresList = null;

    static {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))  //重试策略
                .namespace("registry").build();
        curatorFramework.start();   //启动
        System.out.println("Zookeeper is connected...\n");
        ZKServerDiscovery.curatorFramework = curatorFramework;
    }

    public static String discovery(String serviceName) {
        try {
            String path = "/" + serviceName;
            PathChildrenCache pathChildCache = new PathChildrenCache(curatorFramework, path, false);
            PathChildrenCacheListener listener = (framework, event) -> {
                serviceAddresList = curatorFramework.getChildren().forPath(path);
                System.out.println("节点事件变更：[" + event.getType() + "],重新获取节点列表:" + serviceAddresList.toString());
            };
            pathChildCache.getListenable().addListener(listener);
            pathChildCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);  //首次会从zk获取节点信息
            //负载均衡算法
            LoadBalanceStrategy loadBalance = new RandomLoadBalance();
            String address = ((RandomLoadBalance) loadBalance).selectHost(serviceAddresList);
            System.out.println("负载均衡选择address: " + address);
            if(address == null){
                System.err.println("！！！！！！节点列表信息未选到，再次选择。。。。。。");
            }
            return address;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
