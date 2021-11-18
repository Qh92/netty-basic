package netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;


/**
 * 处理业务逻辑
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/15 15:42
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        int length = msg.getLength();
        //将字节转为字符串
        String message = new String(msg.getContent(), CharsetUtil.UTF_8);
        System.out.println("接收到的客户端信息 : 长度 : " + length + " 内容 : " + message);
        System.out.println("服务器端接收到消息包数量: " + (++count));

        //回送数据给客户端
        /*ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString() + " ", CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);*/
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
