package com.cxq.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author cnxqin
 * @desc
 * @date 2019/08/27 22:31
 */
public class ZookeeperTest {

    private static final String CONNECT_STRING = "CentOS_01:2181";

    public static void main(String[] args){

        try {
//            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STRING, 3000, null);
            //使用自定义的 Watcher 来处理事件
            ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STRING, 5000, (event) -> {
                System.out.println("事件通知" + event.getPath() + " : " + event.getType().name());
            });

            String result = zooKeeper.create("/zookeeper/hello","'hello zookeeper !'".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            System.out.println("result: " + result);

            zooKeeper.exists("/watch",true);    //注册通知

            Thread.sleep(500000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
