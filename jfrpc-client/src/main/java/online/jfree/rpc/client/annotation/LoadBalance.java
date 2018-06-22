package online.jfree.rpc.client.annotation;

import online.jfree.rpc.client.LoadBalanceRule;
import online.jfree.rpc.client.loadbalance.RandomLoanBalanceRule;

import java.lang.annotation.*;

/**
 * @description: 负载均衡注解
 * @author: Guo Lixiao
 * @date 2018-6-8 17:59
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
public @interface LoadBalance {

    /**
     * 负载均衡策略
     * @return
     */
    Class<? extends LoadBalanceRule> value() default RandomLoanBalanceRule.class;

}
