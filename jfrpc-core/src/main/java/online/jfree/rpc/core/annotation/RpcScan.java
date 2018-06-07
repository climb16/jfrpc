package online.jfree.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @description: 扫描服务目录
 * @author: Guo Lixiao
 * @date 2018-6-5 17:43
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcScan {

    String[] basePackages();
}
