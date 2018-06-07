package online.jfree.rpc.common.util;

/**
 * @description: 字符串操作类
 * @author: Guo Lixiao
 * @date 2018-6-4 19:01
 * @since 1.0
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }

    /**
     * 判断字符串数组是否为空
     */
    public static boolean isEmpty(String[] strs) {
        return strs == null || strs.length <= 0;
    }
}
