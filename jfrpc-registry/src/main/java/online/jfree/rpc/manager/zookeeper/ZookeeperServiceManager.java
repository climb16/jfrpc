package online.jfree.rpc.manager.zookeeper;

import online.jfree.rpc.common.util.StringUtil;
import online.jfree.rpc.core.bean.Service;
import online.jfree.rpc.manager.AbstractServiceManager;
import online.jfree.rpc.manager.ServiceManager;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: 基于zookeeper实现的服务管理器
 * @author: Guo Lixiao
 * @date 2018-6-4 15:50
 * @since 1.0
 */
public class ZookeeperServiceManager extends AbstractServiceManager implements ServiceManager {

    private ZookeeperConfig config = new ZookeeperConfig();

    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zookeeper;

    public void init() {
        try {
            //不要关闭zookeeper会话，否则临时节点会被移除
            zookeeper = new ZooKeeper(config.getZkAddress(), config.getSessionTimeout(), new ConnWatcher());
            latch.await();
        } catch (IOException | InterruptedException e) {
            logger.error("zookeeper connection fail", e);
            throw new RuntimeException(e);
        }
        logger.debug("zookeeper connection success");
        createRootPath();
    }

    @Override
    public boolean register(Service service) {
        try {
            //创建服务节点
            String serviceIdPath = config.getRootPath() + "/" + service.getServiceId();
            createNodePath(serviceIdPath, EMPTY_DATA, CreateMode.PERSISTENT);
            //创建实例节点
            String serviceNodePath = createServiceNode(service);
            byte[] data = config.getSerializable().serialize(service);
            createNodePath(serviceNodePath, data, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Service> discover(String serviceId) {
        return discover(serviceId, null);
    }

    @Override
    public List<Service> discover(String serviceId, String version) {
        // 获取 service 节点
        String serviceIdPath = config.getRootPath() + "/" + serviceId;
        List<String> addressList = getChildrenPath(serviceIdPath);
        if (addressList == null || addressList.size() == 0) {
            throw new RuntimeException(String.format("can not find any address node on path: %s", serviceIdPath));
        }
        // 获取 address 节点
        int size = addressList.size();
        List<Service> serviceList = new ArrayList<>(size);
        for (String address : addressList) {
            Service service = new Service();
            service.setServiceId(serviceId);
            this.split(service, address);
            serviceList.add(service);
        }
        // 获取 address 节点的值
        //return getNodePath(serviceIdPath + "/" + serviceNodePath);
        return serviceList;
    }

    private void split(Service service, String address) {
        if (!StringUtil.isEmpty(address)) {
            if (address.contains("&")) {
                String addr[] = address.split("&");
                String host = addr[0];
                String version = addr[1];
                service.setVersion(version);
                if (host.contains(":")) {
                    String hosts[] = host.split(":");
                    String ip = hosts[0];
                    String port = hosts[1];
                    service.setAddress(ip);
                    service.setPort(Integer.parseInt(port));
                }
            }
        }
    }

    private synchronized void createNodePath(String path, byte[] data, CreateMode mode) {
        if (!existsNodePath(path)) {
            try {
                String str = zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
                logger.debug("zookeeper node path [{}] create success [{}]", path, str);
            } catch (KeeperException | InterruptedException e) {
                logger.error("zookeeper node path create fail", e);
                throw new RuntimeException(e);
            }
        }
    }

    private void createRootPath() {
        createNodePath(config.getRootPath(), EMPTY_DATA, CreateMode.PERSISTENT);
    }

    private boolean existsNodePath(String path) {
        try {
            Stat stat = zookeeper.exists(path, false);
            return stat != null;
        } catch (KeeperException | InterruptedException e) {
            logger.error("zookeeper connection fail", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前节点所有子节点
     *
     * @param path
     * @return
     */
    private List<String> getChildrenPath(String path) {
        if (!existsNodePath(path)) {
            logger.error("can not found any service node on serviceId [{}]", path);
            throw new RuntimeException("can not found any service node on serviceId " + path);
        }
        try {
            return zookeeper.getChildren(path, false);
        } catch (KeeperException | InterruptedException e) {
            logger.error("zookeeper connection fail", e);
            throw new RuntimeException(e);
        }
    }

    private Service getNodePath(String path) {
        if (!existsNodePath(path)) {
            logger.error("can not found any service node on serviceId [{}]", path);
            throw new RuntimeException("can not found any service node on serviceId " + path);
        }
        try {
            byte[] bytes = zookeeper.getData(path, false, null);
            Service service = (Service) config.getSerializable().deserialize(bytes);
            return service;
        } catch (KeeperException | InterruptedException e) {
            logger.error("zookeeper connection fail", e);
        }
        return new Service();
    }

    /**
     * 拼装服务实例节点
     *
     * @param service
     * @return
     */
    private String createServiceNode(Service service) {
        String serviceIdPath = config.getRootPath() + "/" + service.getServiceId();
        String serviceNodePath = serviceIdPath + "/" + service.getAddress() + ":" + service.getPort();
        if (!StringUtil.isEmpty(service.getVersion())) {
            serviceNodePath += "&" + service.getVersion();
        }
        return serviceNodePath;
    }

    private class ConnWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            logger.debug("ConnWatcher event status: [{}]", event.getState());
            if (Event.KeeperState.SyncConnected == event.getState()) {
                latch.countDown();
            }
        }
    }

    public ZookeeperConfig getConfig() {
        return config;
    }

    public void setConfig(ZookeeperConfig config) {
        this.config = config;
    }
}