package online.jfree.rpc.core.bean;

/**
 * @description: ${todo}
 * @author: Guo Lixiao
 * @date 2018-6-6 16:36
 * @since 1.0
 */
public class RpcResponse extends RpcBean{

    private Exception exception;
    private Object result;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "exception=" + exception +
                ", result=" + result +
                "} " + super.toString();
    }
}