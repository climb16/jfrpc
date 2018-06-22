package online.jfree.rpc.client;

import online.jfree.rpc.core.bean.Service;

import java.util.List;

/**
 * @description: 负载均衡接口
 * @author: Guo Lixiao
 * @date 2018-6-8 12:49
 * @since 1.0
 */
public interface LoadBalanceRule {

    /**
     * 选举服务实例
     * @param serviceList
     * @return
     */
    Service chooseServer(List<Service> serviceList);

}
