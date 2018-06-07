package online.jfree.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @description: 标记rpc服务端
 * @author: Guo Lixiao
 * @date 2018-6-6 18:00
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcServer {

    /**
     * 端口
     * @return
     */
    int port() default 20211;

}
