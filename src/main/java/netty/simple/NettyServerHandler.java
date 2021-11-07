package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 自定义一个Handler需要继承Netty规定好的HandlerAdapter
 * 这时我们自定义一个Handler，才能称为一个Handler
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-05 22:54
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据的事件（这里我们可以读取客户端发送的消息）
     * @param ctx 上下文对象，含有管道pipeline，通道channel，地址
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务器读取线程: " + Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        System.out.println("channel和pipeline的关系...");
//        Channel channel = ctx.channel();
//        //本质是一个双向链表，出栈入栈
//        ChannelPipeline pipeline = ctx.pipeline();
//
//
//        //将msg 转成ByteBuf,ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是: " + buf.toString(StandardCharsets.UTF_8));
//        System.out.println("客户端地址: " + channel.remoteAddress());

        //这里模拟一个非常耗时的业务，然后采取异步执行，异步执行的方式为提交给该channel对应的NioEventLoop的taskQueue中
//        TimeUnit.SECONDS.sleep(10);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务~", CharsetUtil.UTF_8));


        //解决上述同步执行耗时业务的方案1，用户程序自定义的普通任务
        //如下有两个任务，放在一个队列里面，执行有先后顺序
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务2~", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //方案2，用户自定义定义任务，该任务提交到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务3~", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("业务 continue ....");

    }

    /**
     * 数据读取完毕后的操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存并刷新
        //对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
