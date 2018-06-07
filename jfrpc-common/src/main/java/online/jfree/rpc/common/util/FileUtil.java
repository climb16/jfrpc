package online.jfree.rpc.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * @description: 文件操作类
 * @author: Guo Lixiao
 * @date 2018-6-6 11:26
 * @since 1.0
 */
public final class FileUtil {

    /**
     * 获取当前类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return FileUtil.class.getClassLoader();
    }

    /**
     * 获取系统类加载器
     *
     * @return
     */
    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 获取当前线程所在类的加载器
     *
     * @return
     */
    public static ClassLoader getThreadClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取文件物理路径
     *
     * @param fileName
     * @return
     */
    public static String getFileAsPath(String fileName) {
        return getFileAsURL(fileName).getPath();
    }

    /**
     * 获取文件流
     *
     * @param fileName
     * @return
     */
    public static InputStream getFileAsInputStream(String fileName) {
        return getClassLoader().getResourceAsStream(fileName);
    }

    /**
     * 获取文件URL
     *
     * @param fileName
     * @return
     */
    public static URL getFileAsURL(String fileName) {
        return getClassLoader().getResource(fileName);
    }

    /**
     * 获取文件
     *
     * @param dirName
     * @return
     * @throws IOException
     */
    public static Enumeration<URL> getFilesAsURL(String dirName) throws IOException {
        return getClassLoader().getResources(dirName);
    }
}
