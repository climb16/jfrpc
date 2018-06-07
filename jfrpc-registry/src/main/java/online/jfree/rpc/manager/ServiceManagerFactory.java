package online.jfree.rpc.manager;

import online.jfree.rpc.common.util.StringUtil;
import online.jfree.rpc.core.em.RegistryEnum;
import online.jfree.rpc.manager.zookeeper.ZookeeperConfig;
import online.jfree.rpc.manager.zookeeper.ZookeeperServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: ServiceManager工厂，生产单例ServiceManager对象
 * @author: Guo Lixiao
 * @date 2018-6-5 12:49
 * @since 1.0
 */
public final class ServiceManagerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManagerFactory.class);
    private static Map<RegistryEnum, ServiceManager> serviceManagerMap = new HashMap<>();

    public synchronized static ServiceManager createServiceManager(RegistryEnum managerType, ServiceRegistryConfig config) {
        ServiceManager serviceManager = serviceManagerMap.get(managerType);
        if (serviceManager != null) {
            return serviceManager;
        }
        switch (managerType) {
            case ZOOKEEPER:
                ZookeeperServiceManager zookeeperServiceManager = new ZookeeperServiceManager();
                zookeeperServiceManager.setConfig(config.getZookeeperConfig());
                zookeeperServiceManager.init();
                serviceManager = zookeeperServiceManager;
                break;
            default:
                logger.error("not found managerType: {[]}", managerType);
                throw new RuntimeException("not found managerType " + managerType);
        }
        serviceManagerMap.put(managerType, serviceManager);
        return serviceManager;
    }
}
