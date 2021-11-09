package netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty.heartbeat.MyServerHandler;

import java.util.concurrent.TimeUnit;

/**
 * 实例要求:
 * 1) Http 协议是无状态的, 浏览器和服务器间的请求响应一次，下一次会重新创建连接.
 * 2) 要求：实现基于 webSocket 的长连接的全双工的交互
 * 3) 改变 Http 协议多次请求的约束，实现长连接了， 服务器可以发送消息给浏览器
 * 4) 客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/9 17:36
 */
public class MyServer {
    public static void main(String[] args) {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //在bossGroup增加一个日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供IdleStateHandler
                            /*
                            1.IdleStateHandler 是netty提供的处理空闲状态的处理器
                            long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            long allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                            文档说明：
                            Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                            read, write, or both operation for a while.
                            当IdleStateEvent 触发后，就会传递给管道pipeline下一个handler去处理
                            通过调用（触发）下一个handler的userEventTriggered的方法，在该方法中去处理IdleStateEvent事件（读空闲，写空闲，读写空闲）
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            //加入一个空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(8001).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
