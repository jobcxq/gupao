package com.cxq.rpc.util;

import java.io.Serializable;

/**
 * @author cnxqin
 * @desc 实现rpc远程调用传输数据的类
 * @date 2019/06/23 00:04
 */
public class RpcRequest implements Serializable {

    private String serviceName; //服务名
    private String methodName;  //方法名
    private Object[] args;      //参数
    private String version;     //版本

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
