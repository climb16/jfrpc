package online.jfree.rpc.core;

import online.jfree.rpc.common.util.FileUtil;
import online.jfree.rpc.common.util.StringUtil;
import online.jfree.rpc.core.support.ClassScannerSupport;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @description: 默认类扫描器
 * @author: Guo Lixiao
 * @date 2018-6-6 11:06
 * @since 1.0
 */
public class DefaultClassScanner implements ClassScanner {

    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private static final String PACKAGE_SEPARATOR = ".";

    @Override
    public List<Class<?>> scanClass(String[] basePackages, ClassScannerSupport support) {
        List<Class<?>> classList = new ArrayList<>();
        if (!StringUtil.isEmpty(basePackages)) {
            for (String basePackage : basePackages) {
                scanClass(basePackage, classList, support);
            }
        }
        return classList;
    }

    private void scanClass(String basePackages, List<Class<?>> classList, ClassScannerSupport support){
        try {
            // 从包名获取 URL 类型的资源
            Enumeration<URL> urls = FileUtil.getFilesAsURL(basePackages.replace(PACKAGE_SEPARATOR, DIR_SEPARATOR));
            // 遍历 URL 资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    // 获取协议名（分为 file 与 jar）
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        // 若在 class 目录中，则执行添加类操作
                        String packagePath = url.getPath().replaceAll("%20", " ").replace("%5c", DIR_SEPARATOR);
                        addClass(classList, packagePath, basePackages, support);
                    } else if (protocol.equals("jar")) {
                        // 若在 jar 包中，则解析 jar 包中的 entry
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            // 判断该 entry 是否为 class
                            if (jarEntryName.endsWith(".class")) {
                                // 获取类名
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(PACKAGE_SEPARATOR)).replaceAll(DIR_SEPARATOR, PACKAGE_SEPARATOR);
                                // 执行添加类操作
                                doAddClass(classList, className, support);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClass(List<Class<?>> classList, String packagePath, String packageName, ClassScannerSupport support) {
        try {
            // 获取包名路径下的 class 文件或目录
            File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
            // 遍历文件或目录
            for (File file : files) {
                String fileName = file.getName();
                // 判断是否为文件或目录
                if (file.isFile()) {
                    // 获取类名
                    String className = fileName.substring(0, fileName.lastIndexOf(PACKAGE_SEPARATOR));
                    if (!StringUtil.isEmpty(packageName)) {
                        className = packageName + PACKAGE_SEPARATOR + className;
                    }
                    // 执行添加类操作
                    doAddClass(classList, className, support);
                } else {
                    // 获取子包
                    String subPackagePath = fileName;
                    if (!StringUtil.isEmpty(packagePath)) {
                        subPackagePath = packagePath + DIR_SEPARATOR + subPackagePath;
                    }
                    // 子包名
                    String subPackageName = fileName;
                    if (!StringUtil.isEmpty(packageName)) {
                        subPackageName = packageName + PACKAGE_SEPARATOR + subPackageName;
                    }
                    // 递归调用
                    addClass(classList, subPackagePath, subPackageName, support);
                }
            }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    private void doAddClass(List<Class<?>> classList, String className, ClassScannerSupport support) {
        // 加载类
        Class<?> cls;
        try {
            //反射类，不进行实例化
            cls = Class.forName(className, false, FileUtil.getThreadClassLoader());
            // 判断是否可以添加类
            if (support.support(cls)) {
                classList.add(cls);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
