package netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 1) public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
 *
 * 2) ReplayingDecoder 扩展了 ByteToMessageDecoder 类，使用这个类，我们不必调用 readableBytes()方法。
 * 参数 S 指定了用户状态管理的类型，其中 Void 代表不需要状态管理
 *
 * 3) ReplayingDecoder 使用方便，但它也有一些局限性：
 *  1. 并 不 是 所 有 的 ByteBuf 操 作 都 被 支 持 ， 如 果 调 用 了 一 个 不 被 支 持 的 方 法 ，
 *  将 会 抛 出 一 个 UnsupportedOperationException。
 *  2. ReplayingDecoder 在某些情况下可能稍慢于 ByteToMessageDecoder，
 *  例如网络缓慢并且消息格式复杂时， 消息会被拆成了多个碎片，速度变慢
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/15 10:12
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2#decode 被调用");
        //在ReplayingDecoder不需要判断数据是否足够读取，内部会进行判断
        out.add(in.readLong());
    }
}
