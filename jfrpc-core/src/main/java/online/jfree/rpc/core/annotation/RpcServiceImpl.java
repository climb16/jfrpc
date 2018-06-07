package online.jfree.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @description: 注解一个 rpc 服务实现类
 * @author: Guo Lixiao
 * @date 2018-6-5 16:28
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcServiceImpl {


}