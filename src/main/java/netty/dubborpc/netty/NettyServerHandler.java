package netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.dubborpc.provider.HelloServiceImpl;

/**
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 15:59
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final String PROTOCOL = "helloService#hello#";


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        String message = msg.toString();
        System.out.println("客户端发送的消息 : " + message);
        //双方约定一个协议，eg.要求每次发送消息时，都必须以某个字符串开头helloService#hello
        boolean flag = msg.toString().startsWith(PROTOCOL);
        if (flag) {
            String s = message.split(PROTOCOL)[1];
            String result = new HelloServiceImpl().hello(s);
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
