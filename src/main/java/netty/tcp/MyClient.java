package netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-14 10:57
 */
public class MyClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8001).sync();
            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
