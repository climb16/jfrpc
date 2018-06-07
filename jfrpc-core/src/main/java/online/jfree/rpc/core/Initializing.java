package online.jfree.rpc.core;

/**
 * @description: 引导启动自动加载（spi）
 * @author: Guo Lixiao
 * @date 2018-6-5 19:12
 * @since 1.0
 */
public interface Initializing {

    /**
     * 初始化加载方法
     * @param clazz 启动入口类
     */
    void load(Class clazz);
}
