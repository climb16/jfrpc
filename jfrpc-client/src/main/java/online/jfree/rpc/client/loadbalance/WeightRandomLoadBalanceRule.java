package online.jfree.rpc.client.loadbalance;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.core.bean.Service;

import java.util.List;

/**
 * @description: 权重随机策略
 * @author: Guo Lixiao
 * @date 2018-6-8 17:55
 * @since 1.0
 */
public class WeightRandomLoadBalanceRule implements LoadBalanceRule {
    @Override
    public Service chooseServer(List<Service> serviceList) {

       /* List<String> serverList = new ArrayList<String>();
        while (iterator.hasNext())
        {
            String server = iterator.next();
            int weight = serverMap.get(server);
            for (int i = 0; i < weight; i++)
                serverList.add(server);
        }

        java.util.Random random = new java.util.Random();
        int randomPos = random.nextInt(serverList.size());

        return serverList.get(randomPos);*/
        return null;
    }
}
