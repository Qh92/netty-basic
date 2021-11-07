package netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-06 18:16
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty提供的httpServerCodec codec => [coder - decoder]
        /*
        HttpServerCodec
        1.HttpServerCodec是netty提供的处理http的编解码器
         */
        pipeline.addLast("myHttpServerCodec",new HttpServerCodec());

        //2.增加一个自定义的handler
        pipeline.addLast("myHttpServerHandler",new HttpServerHandler());

    }
}
