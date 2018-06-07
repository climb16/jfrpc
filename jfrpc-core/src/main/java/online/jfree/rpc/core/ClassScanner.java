package online.jfree.rpc.core;

import online.jfree.rpc.core.support.ClassScannerSupport;

import java.util.List;

/**
 * @description: 类扫描器
 * @author: Guo Lixiao
 * @date 2018-6-6 9:54
 * @since 1.0
 */
public interface ClassScanner {

    /**
     * 根据包名扫描类
     *
     * @param basePackages
     * @return
     */
    List<Class<?>> scanClass(String[] basePackages, ClassScannerSupport support);
}
