package online.jfree.rpc.core.annotation;

import online.jfree.rpc.core.em.RegistryEnum;

import java.lang.annotation.*;

/**
 * @description: 注解rpc服务接口，client端用于生成代理类
 * @author: Guo Lixiao
 * @date 2018-6-6 14:36
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RpcService {
    /**
     * 服务id
     * @return
     */
    String serviceId();

    /**
     * 服务名
     * @return
     */
    String serviceName() default "";

    /**
     * 服务端口
     * @return
     */
    //int port() default 20211;

    /**
     * 服务版本
     * @return
     */
    String version() default "";

    /**
     * 服务作者
     * @return
     */
    String author() default "";

    /**
     * 服务描述
     * @return
     */
    String serviceDesc() default "";

    /**
     * 注册中心 默认zk 目前只支持zk
     * @return
     */
    RegistryEnum register() default RegistryEnum.ZOOKEEPER;

    /**
     * 服务注册中心
     * @return
     */
    String zkAddress() default  "127.0.0.1:2080";

    /**
     * 超时时间
     * @return
     */
    int zkSessionTimeout() default 5000;

    /**
     * 服务注册目录
     * @return
     */
    String rootPath() default "/jfrpc";

    /**
     * 服务序列化类
     * @return
     */
    String serializable() default "online.jfree.rpc.common.util.serializable.KryoSerializable";

}
