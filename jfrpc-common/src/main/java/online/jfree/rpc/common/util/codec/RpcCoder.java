package online.jfree.rpc.common.util.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import online.jfree.rpc.common.util.serializable.KryoSerializable;
import online.jfree.rpc.common.util.serializable.ObjectSerializable;

import java.util.List;

/**
 * @description: Rpc默认编码器
 * @author: Guo Lixiao
 * @date 2018-6-6 16:25
 * @since 1.0
 */
public class RpcCoder extends ByteToMessageCodec{

    private ObjectSerializable serializable;

    public RpcCoder(){
        this.serializable = new KryoSerializable();
    }

    public RpcCoder(ObjectSerializable serializable) {
        this.serializable = serializable;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        byte[] data = serializable.serialize(o);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        list.add(serializable.deserialize(data));
    }
}
