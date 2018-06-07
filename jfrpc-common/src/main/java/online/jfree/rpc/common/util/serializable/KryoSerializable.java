package online.jfree.rpc.common.util.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: kryo 序列化
 *
 * @author: Guo Lixiao
 * @date 2018-6-5 10:51
 * @since 1.0
 */
public class KryoSerializable<T> implements ObjectSerializable<T> {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializable.class);

    /** 每个线程的 Kryo 实例 **/
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        //支持对象循环引用（默认true, 否则会栈溢出）
        kryo.setReferences(true);
        //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        kryo.setRegistrationRequired(false);
        //Fix the NPE bug when deserializing Collections.
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    /**
     * 获取当前线程的 kryo 实例
     * @return
     */
    public Kryo getInstance(){
        return kryoLocal.get();
    }

    @Override
    public byte[] serialize(T value) {
        if (value == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, value);
        output.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
            output.close();
        } catch (IOException e) {
            logger.error("KryoSerializable serialize", e);
        }
        return bytes;
    }

    @Override
    public T deserialize(byte[] value) {
        if (value == null || value.length <= 0) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = getInstance();
        T t = (T) kryo.readClassAndObject(input);
        try {
            byteArrayInputStream.close();
            input.close();
        } catch (IOException e) {
            logger.error("KryoSerializable deserialize", e);
        }
        return t;
    }
}
