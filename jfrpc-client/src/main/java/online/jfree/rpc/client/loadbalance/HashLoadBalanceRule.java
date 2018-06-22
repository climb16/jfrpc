package online.jfree.rpc.client.loadbalance;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.common.util.IpUtil;
import online.jfree.rpc.core.bean.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description: 源地址哈希法获取服务实例
 * @author: Guo Lixiao
 * @date 2018-6-8 17:08
 * @since 1.0
 */
public class HashLoadBalanceRule implements LoadBalanceRule {

    private static final Logger logger = LoggerFactory.getLogger(HashLoadBalanceRule.class);

    private static final String IP = IpUtil.getLocalAddress();

    @Override
    public Service chooseServer(List<Service> serviceList) {
        int size = serviceList.size();
        Service service;
        if (size == 1) {
            // 若只有一个地址，则获取该地址
            service = serviceList.get(0);
            logger.debug("get only address node: {}", service);
        } else {
            int hashCode = IP.hashCode();
            int serverListSize = serviceList.size();
            int serverPos = hashCode % serverListSize;
            service = serviceList.get(serverPos);
            logger.debug("get hash address node: {}", service);
        }
        return service;
    }
}
