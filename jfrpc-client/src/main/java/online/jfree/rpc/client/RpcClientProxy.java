package online.jfree.rpc.client;

import net.sf.cglib.proxy.Proxy;
import online.jfree.rpc.core.annotation.RpcService;
import online.jfree.rpc.core.bean.RpcRequest;
import online.jfree.rpc.core.bean.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @description: 代理类, 创建 rpc 客户端代理
 * @author: Guo Lixiao
 * @date 2018-6-7 12:42
 * @since 1.0
 */
public class RpcClientProxy {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private static final RpcClient client = new RpcClient();

    /**
     * 创建 服务代理类
     * @param interfaceClass 服务接口
     * @param <T> 服务代理类
     * @return
     */
    public static <T> T create(final Class<?> interfaceClass){
        return create(interfaceClass, null);
    }

    /**
     * 创建 服务代理类
     * @param interfaceClass 服务接口
     * @param loadBalanceRule 负载策略
     * @param <T> 服务代理类
     * @return
     */
    public static <T> T create(final Class<?> interfaceClass, LoadBalanceRule loadBalanceRule) {
        final RpcService rpcService = interfaceClass.getAnnotation(RpcService.class);
        if (rpcService == null) {
            throw new RuntimeException("interfaceClass : [ " + interfaceClass + " ], not JFRpcServer");
        }
        client.setLoadBalanceRule(loadBalanceRule);
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    // 创建 RPC 请求对象并设置请求属性
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setServiceId(rpcService.serviceId());
                    request.setInterfaceName(method.getDeclaringClass().getName());
                    request.setServiceVersion(rpcService.version());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    // 创建 RPC 客户端对象并发送 RPC 请求
                    long time = System.currentTimeMillis();
                    RpcResponse response = client.send(request, rpcService);
                    logger.debug("rpc process time: {}ms", System.currentTimeMillis() - time);
                    if (response == null) {
                        throw new RuntimeException("response is null");
                    }
                    // 返回 RPC 响应结果
                    if (response.getException() != null) {
                        throw response.getException();
                    } else {
                        return response.getResult();
                    }
                }
        );
    }
}
