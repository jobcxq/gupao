package com.cxq.rpc.discovery;

import java.util.List;

/**
 * @author cnxqin
 * @desc 负载均衡策略
 * @date 2019/08/25 14:47
 */
public interface LoadBalanceStrategy {

    String selectHost(List<String> hostList);
}
