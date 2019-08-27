package com.cxq.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

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
        nodeCache.getListenable().addListener(listener);
//        nodeCache.start(true);    //NodeCache第一次启动就会立刻从zk读取节点数据并保存在Cache中
        nodeCache.start();          //默认false
        Thread.sleep(60000);
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
//        pathChildCache.start(PathChildrenCache.StartMode.NORMAL);
        Thread.sleep(60000);
    }

    @Test
    public void leaderSelector() throws Exception {
        //Master 选举
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework,
                "/leader", new LeaderSelectorListenerAdapter() {
            //会在节点下创建临时顺序节点来按顺序执行
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println(System.currentTimeMillis()%100000 + ": master 选举成功，开始执行 master 任务");
                Thread.sleep(5000);
                //一旦执行完该方法，即释放master的权力，再开始新的一轮选举
                System.out.println(System.currentTimeMillis()%100000 + ": master 任务完成，释放 master 权力");
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        Thread.sleep(60000);
    }

    @Test
    public void processLock() throws InterruptedException {
        //分布式锁
        final InterProcessLock lock = new InterProcessMutex(curatorFramework,"/lock");
        Thread.sleep(2000);
        int count = 5;     //并发数
        //模拟并发
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for(int i = 0; i < count; i ++){
            new Thread(() -> {
                try {
                    countDownLatch.await(); //等待
                    lock.acquire();     //  争抢锁
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                System.out.println(Thread.currentThread().getName() + " 订单号： "
                        + df.format(new Date()));
                try {
                    lock.release();         //释放锁
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
            countDownLatch.countDown();
        }

        Thread.sleep(60000);
    }

    @Test
    public void ZKPaths() throws Exception {
        ZooKeeper zooKeeper = curatorFramework.getZookeeperClient().getZooKeeper();
        ZKPaths.mkdirs(zooKeeper,"/zkpath/father/son");
    }

    @Test
    public void createWithAcl() throws Exception {
        List<ACL> aclList = new ArrayList<>();
        aclList.add(new ACL(ZooDefs.Perms.ALL,new Id("digest",DigestAuthenticationProvider.generateDigest("root:1234"))));
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .withACL(aclList)
                .forPath("/acl","cxq".getBytes());
        System.out.println("create success!");
    }

}
