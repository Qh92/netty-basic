package netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Qh
 * @version 1.0
 * @date 2021/11/15 15:38
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("客户端接收到消息: " + message);
        System.out.println("客户端接收到消息的次数: " + (++count));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据
        for (int i = 0; i < 10; i++) {
            ByteBuf buf = Unpooled.copiedBuffer("hello,server : " + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
