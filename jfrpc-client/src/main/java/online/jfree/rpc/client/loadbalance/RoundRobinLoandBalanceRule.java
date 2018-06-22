package online.jfree.rpc.client.loadbalance;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.core.bean.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description: 轮询选举服务实例
 * @author: Guo Lixiao
 * @date 2018-6-8 16:52
 * @since 1.0
 */
public class RoundRobinLoandBalanceRule implements LoadBalanceRule {

    private static final Logger logger = LoggerFactory.getLogger(RoundRobinLoandBalanceRule.class);

    private static Integer pos = 0;

    @Override
    public Service chooseServer(List<Service> serviceList) {
        int size = serviceList.size();
        Service service;
        if (size == 1) {
            // 若只有一个地址，则获取该地址
            service = serviceList.get(0);
            logger.debug("get only address node: {}", service);
        } else {
            //轮询获取该地址
            synchronized (pos) {
                if (pos > serviceList.size()) {
                    pos = 0;
                }
                service = serviceList.get(pos);
                pos++;
            }
            logger.debug("get round robin address node: {}", service);
        }
        return service;
    }
}
