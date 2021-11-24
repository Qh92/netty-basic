package netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 16:52
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    /** 返回的结果 */
    private String result;

    /** 调用的参数 */
    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 被调用");
        context = ctx;
        
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用");
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    /**
     * 被代理对象调用，真正发送数据给服务器，发送完毕后，wait,等待被唤醒
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1 被调用");
        context.writeAndFlush(param);
        wait();
        System.out.println("call2 被调用");
        return result;
    }

    public void setParam(String param) {
        System.out.println("setParam 被调用");
        this.param = param;
    }
}
