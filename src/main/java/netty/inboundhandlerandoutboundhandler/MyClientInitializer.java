package netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-14 11:00
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());
        //入站的handler
        pipeline.addLast(new MyByteToLongDecoder());
        //加入一个自定义的handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
