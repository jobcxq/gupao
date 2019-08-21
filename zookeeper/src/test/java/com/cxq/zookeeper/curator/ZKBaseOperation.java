package com.cxq.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
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
}
