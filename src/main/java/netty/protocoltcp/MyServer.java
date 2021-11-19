package netty.protocoltcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TCP 粘包和拆包解决方案
 * 1) 使用自定义协议 + 编解码器 来解决
 * 2) 关键就是要解决 服务器端每次读取数据长度的问题,
 * 这个问题解决，就不会出现服务器多读或少读数据的问 题，从而避免的 TCP 粘包、拆包
 *
 * 看一个具体的实例:
 * 1) 要求客户端发送 5 个 Message 对象, 客户端每次发送一个 Message 对象
 * 2) 服务器端每次接收一个 Message, 分 5 次进行解码， 每读取到 一个 Message , 会回复一个 Message 对象 给客 户端
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-14 10:33
 */
public class MyServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8001).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
