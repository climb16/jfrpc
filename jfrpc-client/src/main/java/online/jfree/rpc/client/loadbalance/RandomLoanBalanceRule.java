package online.jfree.rpc.client.loadbalance;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.core.bean.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: 随机选举策略
 * @author: Guo Lixiao
 * @date 2018-6-8 12:56
 * @since 1.0
 */
public class RandomLoanBalanceRule implements LoadBalanceRule {

    private static final Logger logger = LoggerFactory.getLogger(RandomLoanBalanceRule.class);

    @Override
    public Service chooseServer(List<Service> serviceList) {
        int size = serviceList.size();
        Service service;
        if (size == 1) {
            // 若只有一个地址，则获取该地址
            service = serviceList.get(0);
            logger.debug("get only address node: {}", service);
        } else {
            // 若存在多个地址，则随机获取一个地址
            service = serviceList.get(ThreadLocalRandom.current().nextInt(size));
            logger.debug("get random address node: {}", service);
        }
        return service;
    }
}
