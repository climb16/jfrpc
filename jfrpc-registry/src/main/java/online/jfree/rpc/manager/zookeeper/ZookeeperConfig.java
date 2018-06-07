package online.jfree.rpc.manager.zookeeper;

import online.jfree.rpc.common.util.serializable.KryoSerializable;
import online.jfree.rpc.common.util.serializable.ObjectSerializable;

/**
 * @description: zookeeper 配置类
 * @author: Guo Lixiao
 * @date 2018-6-5 11:47
 * @since 1.0
 */
public class ZookeeperConfig {

    private static final int DEF_SESSION_TIMEOUT = 5000;
    private static final String DEF_ROOT_PATH = "/jfrpc";

    /**
     * session 超时参数, 即在 sessionTimeout时间内 zk服务端未收到心跳则断开会话链接
     */
    private int sessionTimeout = DEF_SESSION_TIMEOUT;

    private String zkAddress = "127.0.0.1:2080";
    private String rootPath = DEF_ROOT_PATH;
    private ObjectSerializable serializable = new KryoSerializable();

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public ObjectSerializable getSerializable() {
        return serializable;
    }

    public void setSerializable(ObjectSerializable serializable) {
        this.serializable = serializable;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
