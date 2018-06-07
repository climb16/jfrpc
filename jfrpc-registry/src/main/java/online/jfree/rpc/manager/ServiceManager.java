package online.jfree.rpc.manager;


import online.jfree.rpc.core.bean.Service;

/**
 * @description: 服务管理器，提供服务注册和发现能力
 * @author: Guo Lixiao
 * @date 2018-6-4 15:25
 * @since 1.0
 */
public interface ServiceManager {

    /**
     * 服务注册
     * @param service 服务
     * @return
     */
    boolean register(Service service);

    /**
     * 服务发现
     * @param serviceId 服务id
     * @return 服务实例
     */
    Service discover(String serviceId);

    /**
     * 服务发现
     * @param serviceId 服务id
     * @param version 服务版本
     * @return 服务实例
     */
    Service discover(String serviceId, String version);
}
