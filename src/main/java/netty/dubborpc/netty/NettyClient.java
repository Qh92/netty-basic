package netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 17:08
 */
public class NettyClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;


    /**
     * 返回一个代理对象
     * @param serviceClass
     * @param providerName
     * @return
     */
    public Object getBean(final Class<?> serviceClass,final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, (proxy, method, args) -> {
            if (client == null) {
                initClient();
            }
            //发送给提供方的消息，providerName 协议头，args[0] 具体发送的消息
            client.setParam(providerName + args[0]);
            return executorService.submit(client).get();
        });
    }


    /**
     * 初始化客户端
     */
    private static void initClient() {
        client = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(client);
                        }
                    });

            bootstrap.connect("127.0.0.1", 8001).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //group.shutdownGracefully();
        }
    }


}
