package netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-05 22:26
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
        创建BossGroup和WorkerGroup
        1.创建两个线程循环组 bossGroup和workerGroup
        2.bossGroup只是处理连接请求，workerGroup处理客户端业务
        3.两个都是无限循环
        4.bossGroup和workerGroup含有的子线程（NioEventLoop）的个数，默认是 CPU核数 * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            /*
            Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，主要作用是配置整个 Netty 程序，串联 各个组件，
            Netty 中 Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类
             */
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            /*
            1.public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)，该方法用于服务器端， 用来设置两个 EventLoopGroup
            2.public B group(EventLoopGroup group) ，该方法用于客户端，用来设置一个 EventLoopGroup
            3.public B channel(Class<? extends C> channelClass)，该方法用来设置一个服务器端的通道实现
            4.public <T> B option(ChannelOption<T> option, T value)，用来给 ServerChannel 添加配置
            5.public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)，用来给接收到的通道添加配置
            6.public ServerBootstrap childHandler(ChannelHandler childHandler)，该方法用来设置业务处理类（自定义的 handler）
            7.public ChannelFuture bind(int inetPort) ，该方法用于服务器端，用来设置占用的端口号
            8.public ChannelFuture connect(String inetHost, int inetPort) ，该方法用于客户端，用来连接服务器端
             */
            //使用链式编程进行设置
            //设置两个线程组
            bootstrap.group(bossGroup,workerGroup)
                    //使用NioSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //该handler对应bossGroup,workerGroup对应childHandler
                    //.handler(null)
                    //给workGroup的EventLoop对应的管道设置处理器
                    //创建一个通道初始化对象（匿名对象）
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 给pipeline设置处理器
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /*
                            可以使用一个集合管理SocketChannel，在推送消息时，可以将业务加入到各个channel对应的NioEventLoop的TaskQueue或者scheduleTaskQueue
                             */
                            System.out.println("客户socketChannel hashcode = " + ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务器 is ready.....");
            /*
            1) 当 Future 对象刚刚创建时，处于非完成状态，调用者可以通过返回的 ChannelFuture 来获取操作执行的状态， 注册监听函数来执行完成后的操作。
            2) 常见有如下操作
                 通过 isDone 方法来判断当前操作是否完成；
                 通过 isSuccess 方法来判断已完成的当前操作是否成功；
                 通过 getCause 方法来获取已完成的当前操作失败的原因；
                 通过 isCancelled 方法来判断已完成的当前操作是否被取消；
                 通过 addListener 方法来注册监听器，当操作已完成(isDone 方法返回完成)，将会通知指定的监听器；如果 Future 对象已完成，则通知指定的监听器
             */
            //绑定一个端口并且同步，生成一个ChannelFuture对象，启动服务器（绑定端口）
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            //给channelFuture注册监听器，监控我们关心的事件
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()){
                    System.out.println("监听端口 6668 成功");
                }else {
                    System.out.println("监听端口 6668 失败");
                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭事件组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
