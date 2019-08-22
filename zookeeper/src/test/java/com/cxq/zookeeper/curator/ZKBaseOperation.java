package com.cxq.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

/**
 * @author cnxqin
 * @desc zk的基本操作
 * @date 2019/08/21 22:44
 */
public class ZKBaseOperation {

    private static final String NAMESPACE = "curator";

    private CuratorFramework curatorFramework = ZKConnection.getConnection(NAMESPACE);

    @Test
    public void create() throws Exception {
        //创建
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).
                forPath("/data","cxq".getBytes());
        System.out.println("create success!");
    }


    @Test
    public void getData() throws Exception {
        //查询
        byte[] bytes = curatorFramework.getData().forPath("/data");
        System.out.println(String.valueOf(bytes));
    }

    @Test
    public void setData() throws Exception {
        //更新
        Stat stat = curatorFramework.setData().forPath("/data","helloworld".getBytes());
        System.out.println(stat.toString());
    }

    @Test
    public void delete() throws Exception {
        //删除
        curatorFramework.delete().forPath("/data");
        System.out.println("delete success!");
    }



    @Test
    public void addListenerWithNode() throws Exception {
        //配置中心：创建、修改、删除
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener listener = () -> {
            System.out.println("receive Node Changed");
            System.out.println(nodeCache.getCurrentData().getPath() + " : "
                    + new String(nodeCache.getCurrentData().getData()));
        };
//        nodeCache.start(true);    //NodeCache第一次启动就会立刻从zk读取节点数据并保存在Cache中
        nodeCache.start();          //默认false
    }

    @Test
    public void addListenerWithChildNode() throws Exception {
        //服务注册中心：可以针对服务做动态感知
        PathChildrenCache pathChildCache = new PathChildrenCache(curatorFramework,"/watch/children",false);
        PathChildrenCacheListener listener = (framework, event) -> {
            System.out.println("receive Node Changed");
            System.out.println(event.getType() + " : "  //CHILD_ADDED、CHILD_UPDATED、CHILD_REMOVED
                    + new String(event.getData().getData()));
        };
        pathChildCache.getListenable().addListener(listener);
        pathChildCache.start();
        pathChildCache.start(PathChildrenCache.StartMode.NORMAL);
    }

}
