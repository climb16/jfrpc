package online.jfree.rpc.core;

import online.jfree.rpc.common.util.StringUtil;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * @description: 启动类, 扫描服务目录
 * @author: Guo Lixiao
 * @date 2018-6-5 18:03
 * @since 1.0
 */
public class RpcBootstrap {

    /**
     * 加载主类
     *
     * @param clazz
     * @param args
     */
    public static void run(final Class clazz, String[] args) {
        //加载系统变量
        Properties properties = System.getProperties();
        for (String str : args){
            if (!StringUtil.isEmpty(str)) {
                String[] hash = str.split("=");
                if (hash.length == 2){
                    properties.setProperty(hash[0], hash[1]);
                }
            }
        }
        //加载初始类
        new Thread(() -> {
            ServiceLoader<Initializing> initializes = ServiceLoader.load(Initializing.class);
            for (Initializing initializing : initializes) {
                initializing.load(clazz);
            }
        }).start();
    }

}
