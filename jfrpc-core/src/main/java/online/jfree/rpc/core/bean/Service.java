package online.jfree.rpc.core.bean;

import online.jfree.rpc.common.util.StringUtil;

/**
 * @description: 服务属性
 * @author: Guo Lixiao
 * @date 2018-6-4 15:37
 * @since 1.0
 */
public class Service implements java.io.Serializable {
    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务地址
     */
    private String address = "127.0.0.1";

    /**
     * 服务端口 默认20211
     */
    private int port = 20211;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 描述
     */
    private String serviceDesc;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        if (!StringUtil.isEmpty(serviceId)) {
            this.serviceId = serviceId;
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        if (!StringUtil.isEmpty(serviceName)) {
            this.serviceName = serviceName;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!StringUtil.isEmpty(address)) {
            this.address = address;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port > 0) {
            this.port = port;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (!StringUtil.isEmpty(version)) {
            this.version = version;
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (!StringUtil.isEmpty(author)) {
            this.author = author;
        }
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        if (!StringUtil.isEmpty(serviceDesc)) {
            this.serviceDesc = serviceDesc;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Service{");
        sb.append("serviceId='").append(serviceId).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", port=").append(port);
        sb.append(", version='").append(version).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", serviceDesc='").append(serviceDesc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}