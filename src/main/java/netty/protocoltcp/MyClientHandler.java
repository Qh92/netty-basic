package netty.protocoltcp;

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
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        /*byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("客户端接收到消息: " + message);
        System.out.println("客户端接收到消息的次数: " + (++count));*/

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据
        for (int i = 0; i < 10; i++) {
            String message = "hello,服务器,我是客户端" + i;
            byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
            int length = bytes.length;
            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLength(length);
            messageProtocol.setContent(bytes);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
