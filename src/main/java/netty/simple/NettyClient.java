package netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-06 12:47
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的是Bootstrap而不是ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端通道的实现类（反射）
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //加入自己的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端 is ok ....");

            //启动客户端去连接服务器端
            //关于ChannelFuture要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6668).sync();
            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }


    }
}
