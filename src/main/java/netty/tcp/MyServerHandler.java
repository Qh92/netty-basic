package netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Random;
import java.util.UUID;


/**
 * @author Qh
 * @version 1.0
 * @date 2021/11/15 15:42
 */
public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        //将buffer转为字符串
        String message = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("服务器端接收到消息量: " + (++count));
        System.out.println("接收到的数据 : " + message);

        //回送数据给客户端
        ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString() + " ", CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
