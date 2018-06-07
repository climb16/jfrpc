package online.jfree.rpc.common.util.serializable;

/**
 * @description: 序列化接口
 * @author: Guo Lixiao
 * @date 2018-6-5 9:54
 * @since 1.0
 */
public interface ObjectSerializable<T> {

    /**
     * 序列化
     * @param value
     * @return
     */
    byte[] serialize(T value);

    /**
     * 反序列化
     * @param value
     * @return
     */
    T deserialize(byte[] value);
}
