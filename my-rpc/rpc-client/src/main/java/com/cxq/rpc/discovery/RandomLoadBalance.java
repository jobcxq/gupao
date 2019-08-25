package com.cxq.rpc.discovery;

import java.util.List;
import java.util.Random;

/**
 * @author cnxqin
 * @desc 随机的负载均衡算法
 * @date 2019/08/25 14:52
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> hostList) {
        Random random = new Random();
        return hostList.get(random.nextInt(hostList.size()));
    }
}
