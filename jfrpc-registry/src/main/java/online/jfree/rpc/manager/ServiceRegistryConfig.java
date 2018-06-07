package online.jfree.rpc.manager;

import online.jfree.rpc.manager.zookeeper.ZookeeperConfig;

/**
 * @description: ${todo}
 * @author: Guo Lixiao
 * @date 2018-6-7 9:31
 * @since 1.0
 */
public class ServiceRegistryConfig {

    private ZookeeperConfig zookeeperConfig;

    public ZookeeperConfig getZookeeperConfig() {
        return zookeeperConfig;
    }

    public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }
}