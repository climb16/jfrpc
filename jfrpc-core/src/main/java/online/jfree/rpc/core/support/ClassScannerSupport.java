package online.jfree.rpc.core.support;

/**
 * @description: 类扫描器模板
 * @author: Guo Lixiao
 * @date 2018-6-6 11:50
 * @since 1.0
 */
public interface ClassScannerSupport {

    /**
     * 时候允许添加类
     * @param cls
     * @return
     */
    boolean support(Class<?> cls);
}
