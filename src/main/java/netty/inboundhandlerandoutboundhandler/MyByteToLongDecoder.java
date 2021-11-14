package netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-14 10:42
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     * decode 会根据接收到的数据被调用多次，直到确定没有新的数据被添加到list，或者ByteBuf没有更多的可读字节为止
     * 如果list out 不为空，就会将list的内容传递给下一个channelInboundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in 入站的ByteBuf
     * @param out List集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder#decode 被调用");
        //long 8个字节
        if (in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
