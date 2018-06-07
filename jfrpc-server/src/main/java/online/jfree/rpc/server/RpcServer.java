package online.jfree.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import online.jfree.rpc.common.util.IpUtil;
import online.jfree.rpc.common.util.codec.RpcCoder;
import online.jfree.rpc.common.util.serializable.ObjectSerializable;
import online.jfree.rpc.core.annotation.RpcService;
import online.jfree.rpc.core.bean.Service;
import online.jfree.rpc.manager.ServiceManager;
import online.jfree.rpc.manager.ServiceManagerFactory;
import online.jfree.rpc.manager.ServiceRegistryConfig;
import online.jfree.rpc.manager.zookeeper.ZookeeperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 服务发布类
 * @author: Guo Lixiao
 * @date 2018-6-5 17:39
 * @since 1.0
 */
public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private final static String localIp = IpUtil.getLocalAddress();

    private Map<String, Map<String, Object>> serviceBeanMap = new ConcurrentHashMap<>();

    private ServiceRegistryConfig registryConfig = new ServiceRegistryConfig();

    void provider(List<Class<?>> cls, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    // 设置编码器
                    pipeline.addLast(new RpcCoder());
                    // 处理 RPC 请求
                    pipeline.addLast(new RpcServerHandler(serviceBeanMap));
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            String ip = localIp;
            // 启动 RPC 服务器
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            for (Class<?> cl : cls) {
                Class[] infs = cl.getInterfaces();
                RpcService rpcService = null;
                for (Class inf : infs) {
                    rpcService = inf.isAnnotationPresent(RpcService.class) ? (RpcService) inf.getAnnotation(RpcService.class) : null;
                }
                if (rpcService == null) {
                    throw new RuntimeException("PrcServiceImpl [ " + cl + " ] does not implement the interface that is annotated by @RpcService, or does not inherit superclass which is annotated by @RpcService ");
                }
                String serviceId = rpcService.serviceId();
                String serviceName = rpcService.serviceName();
                String version = rpcService.version();
                String author = rpcService.author();
                String serviceDesc = rpcService.serviceDesc();
                Service service = new Service();
                service.setServiceId(serviceId);
                service.setServiceName(serviceName);
                service.setAddress(ip);
                service.setPort(port);
                service.setAuthor(author);
                service.setVersion(version);
                service.setServiceDesc(serviceDesc);

                //本地存储bean对象
                if (!serviceBeanMap.containsKey(serviceId)) {
                    Map<String, Object> map = new HashMap(1);
                    map.put(version, cl.newInstance());
                    serviceBeanMap.put(serviceId, map);
                } else {
                    Map<String, Object> map = serviceBeanMap.get(serviceId);
                    if (!map.containsKey(version)) {
                        map.put(version, cl.newInstance());
                    }else {
                        throw new RuntimeException("serviceId : [ " + serviceId + " ] already exists");
                    }
                }
                // 注册 RPC 服务地址
                ZookeeperConfig zookeeperConfig = new ZookeeperConfig();
                zookeeperConfig.setZkAddress(rpcService.zkAddress());
                zookeeperConfig.setSessionTimeout(rpcService.zkSessionTimeout());
                zookeeperConfig.setRootPath(rpcService.rootPath());
                String serializablePath = rpcService.serializable();
                try {
                    ObjectSerializable serializable = (ObjectSerializable) Class.forName(serializablePath).newInstance();
                    zookeeperConfig.setSerializable(serializable);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                registryConfig.setZookeeperConfig(zookeeperConfig);
                ServiceManager serviceRegistry = ServiceManagerFactory.createServiceManager(rpcService.register(), registryConfig);
                serviceRegistry.register(service);
                logger.debug("register service: {} => {}", cl.getName(), service.toString());
            }
            logger.debug("server started on port {}", port);
            // 关闭 RPC 服务器(会阻塞当前线程)
            future.channel().closeFuture().sync();
        } catch (InterruptedException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}