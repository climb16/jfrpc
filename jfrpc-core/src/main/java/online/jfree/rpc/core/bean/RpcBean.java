package online.jfree.rpc.core.bean;

/**
 * @description:
 * @author: Guo Lixiao
 * @date 2018-6-7 13:37
 * @since 1.0
 */
public class RpcBean implements java.io.Serializable {

    private String requestId;
    private String serviceId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcBean{");
        sb.append("requestId='").append(requestId).append('\'');
        sb.append(", serviceId='").append(serviceId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}