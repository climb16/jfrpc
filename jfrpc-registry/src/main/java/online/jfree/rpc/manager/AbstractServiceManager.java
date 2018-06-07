package online.jfree.rpc.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: ServiceManager 抽象类，定义公共的方法
 * @author: Guo Lixiao
 * @date 2018-6-5 11:41
 * @since 1.0
 */
public abstract class AbstractServiceManager implements ServiceManager{

    protected static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    protected static final byte[] EMPTY_DATA = new byte[0];
    protected boolean rootExists = false;

}