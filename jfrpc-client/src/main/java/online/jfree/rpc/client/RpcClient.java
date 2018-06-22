package online.jfree.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import online.jfree.rpc.client.loadbalance.RandomLoanBalanceRule;
import online.jfree.rpc.common.util.codec.RpcCoder;
import online.jfree.rpc.common.util.serializable.ObjectSerializable;
import online.jfree.rpc.core.annotation.RpcService;
import online.jfree.rpc.core.bean.RpcRequest;
import online.jfree.rpc.core.bean.RpcResponse;
import online.jfree.rpc.core.bean.Service;
import online.jfree.rpc.manager.ServiceManager;
import online.jfree.rpc.manager.ServiceManagerFactory;
import online.jfree.rpc.manager.ServiceRegistryConfig;
import online.jfree.rpc.manager.zookeeper.ZookeeperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description:
 * @author: Guo Lixiao
 * @date 2018-6-7 11:49
 * @since 1.0
 */
public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private ServiceRegistryConfig registryConfig = new ServiceRegistryConfig();
    private LoadBalanceRule loadBalanceRule = new RandomLoanBalanceRule();

    public RpcResponse send(RpcRequest request, RpcService rpcService){
        //  RPC 服务注中心配置
        final ZookeeperConfig[] zookeeperConfig = {new ZookeeperConfig()};
        zookeeperConfig[0].setZkAddress(rpcService.zkAddress());
        zookeeperConfig[0].setSessionTimeout(rpcService.zkSessionTimeout());
        zookeeperConfig[0].setRootPath(rpcService.rootPath());
        String serializablePath = rpcService.serializable();
        try {
            ObjectSerializable serializable = (ObjectSerializable) Class.forName(serializablePath).newInstance();
            zookeeperConfig[0].setSerializable(serializable);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        registryConfig.setZookeeperConfig(zookeeperConfig[0]);
        ServiceManager serviceRegistry = ServiceManagerFactory.createServiceManager(rpcService.register(), registryConfig);
        List<Service> services = serviceRegistry.discover(rpcService.serviceId(), rpcService.version());

        /**  负载均衡选举 **/
        if (loadBalanceRule == null) {
            loadBalanceRule = new RandomLoanBalanceRule();
        }
        Service service = loadBalanceRule.chooseServer(services);
        logger.debug("discover service: {} => {}", service.toString());
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final RpcResponse[] response = new RpcResponse[1];
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    // 编码 RPC 请求
                    pipeline.addLast(new RpcCoder());
                    // 处理 RPC 响应
                    pipeline.addLast(new SimpleChannelInboundHandler() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ctx.writeAndFlush(request);
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) {
                            response[0] = (RpcResponse) o;
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            ctx.close();
                        }
                    });
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(service.getAddress(), service.getPort()).sync();
            // 写入 RPC 请求数据并关闭连接
            future.channel().closeFuture().sync();
            // 返回 RPC 响应对象
            return response[0];
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

    public void setLoadBalanceRule(LoadBalanceRule loadBalanceRule) {
        this.loadBalanceRule = loadBalanceRule;
    }
}