package online.jfree.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import online.jfree.rpc.core.bean.RpcRequest;
import online.jfree.rpc.core.bean.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: 服务处理类，反射调用服务接口
 * @author: Guo Lixiao
 * @date 2018-6-6 16:48
 * @since 1.0
 */
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    public final Map<String, Map<String, Object>> serviceBeanMap ;

    public RpcServerHandler(Map serviceBeanMap){
        this.serviceBeanMap = serviceBeanMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) {
        // 创建并初始化 RPC 响应对象
        RpcResponse response = new RpcResponse();
        RpcRequest request = (RpcRequest) o;
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Exception e) {
            logger.error("handle result failure", e);
            response.setException(e);
        }
        // 写入 RPC 响应对象并自动关闭连接
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws Exception {
        // 获取服务对象
        Map<String, Object> serviceBeans = serviceBeanMap.get(request.getServiceId());
        if (serviceBeans == null) {
            throw new RuntimeException(String.format("can not find service bean by key: [%s], version: [%s]", request.getServiceId(), request.getServiceVersion()));
        }
        Object serviceBean = serviceBeans.get(request.getServiceVersion());
        if (serviceBeans == null) {
            throw new RuntimeException(String.format("can not find service bean by key: [%s], version: [%s]", request.getServiceId(), request.getServiceVersion()));
        }
        // 获取反射调用所需的参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // 执行反射调用
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);
        // 使用 CGLib 执行反射调用
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }
}
