package online.jfree.rpc.server;

import online.jfree.rpc.common.util.StringUtil;
import online.jfree.rpc.core.ClassScanner;
import online.jfree.rpc.core.DefaultClassScanner;
import online.jfree.rpc.core.Initializing;
import online.jfree.rpc.core.annotation.*;
import online.jfree.rpc.core.annotation.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description: 初始化服务发布类
 * @author: Guo Lixiao
 * @date 2018-6-6 9:40
 * @since 1.0
 */
public class RpcServerInitializing implements Initializing {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerInitializing.class);
    protected ClassScanner scanner = new DefaultClassScanner();
    private online.jfree.rpc.server.RpcServer rpcServer = new online.jfree.rpc.server.RpcServer();

    @Override
    public void load(Class clazz) {
        RpcScan rpcScan = (RpcScan) clazz.getAnnotation(RpcScan.class);
        RpcServer rpcServer = (RpcServer) clazz.getAnnotation(RpcServer.class);
        List<Class<?>> classList = null;
        if (rpcServer != null && rpcScan != null) {
            String[] basePackages = rpcScan.basePackages();
            logger.debug("Load RpcScan basePackages [{}]", basePackages);
            if (!StringUtil.isEmpty(basePackages)) {
                classList = scanner.scanClass(basePackages, cls -> {
                    RpcServiceImpl rpcService = cls.getAnnotation(RpcServiceImpl.class);
                    return rpcService != null;
                });
            } else {
                logger.info("RpcServerInitializing RpcScan basePackages empty");
            }
        }
        if (classList != null && classList.size() > 0 ) {
            //发布服务
            this.rpcServer.provider(classList, rpcServer.port());
            logger.info("classList: {}", classList);
        }

    }
}