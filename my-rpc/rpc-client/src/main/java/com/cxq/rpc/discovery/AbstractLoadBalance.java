package com.cxq.rpc.discovery;

import java.util.List;

/**
 * @author cnxqin
 * @desc
 * @date 2019/08/25 14:49
 */
public abstract class AbstractLoadBalance implements LoadBalanceStrategy{
    @Override
    public String selectHost(final List<String> hostList) {
        System.out.println("负载均衡，节点列表信息：" + hostList.toString());
        if(hostList == null || hostList.size() == 0) {
            return null;
        }
        if(hostList.size() == 1){
            return hostList.get(0);
        }
        return doSelect(hostList);
    }

    protected abstract String doSelect(List<String> hostList);
}
