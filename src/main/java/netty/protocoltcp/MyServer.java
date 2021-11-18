package netty.protocoltcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实例要求:
 * 1) 使用自定义的编码器和解码器来说明 Netty 的 handler 调用机制
 *  客户端发送 long -> 服务器
 *  服务端发送 long -> 客户端
 *
 * 结论：
 * 1.不论解码器 handler 还是 编码器 handler 即接收的消息类型必须与待处理的消息类型一致，
 * 否则该 handler 不 会被执行
 * 2.在解码器 进行数据解码时，需要判断 缓存区(ByteBuf)的数据是否足够 ，
 * 否则接收到的结果会期望结果可能 不一致
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
