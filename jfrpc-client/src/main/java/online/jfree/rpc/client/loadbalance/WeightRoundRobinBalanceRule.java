package online.jfree.rpc.client.loadbalance;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.core.bean.Service;

import java.util.List;

/**
 * @description: 权重轮询
 * @author: Guo Lixiao
 * @date 2018-6-8 17:49
 * @since 1.0
 */
public class WeightRoundRobinBalanceRule implements LoadBalanceRule {
    @Override
    public Service chooseServer(List<Service> serviceList) {


       /* while (iterator.hasNext())
        {
            String server = iterator.next();
            int weight = serverMap.get(server);
            for (int i = 0; i < weight; i++)
                serverList.add(server);
        }

        String server = null;
        synchronized (pos)
        {
            if (pos > keySet.size())
                pos = 0;
            server = serverList.get(pos);
            pos ++;
        }*/
        return null;
    }
}
