package netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-07 20:58
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 如果想实现点对点发送，可以使用Map来存储
     */
    private static Map<String,Channel> channels = new HashMap<>();

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接建立，一旦连接，第一个被执行
     * 将当前channel加入到channelGroup中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        /*
        该方法会将channelGroup中的所有的channel遍历并发送消息，我们不需要再遍历
         */
        channelGroup.writeAndFlush("[客户端] : " + channel.remoteAddress() + " 加入聊天[" + LocalDateTime.now().format(formatter) + "]\n");
        channelGroup.add(channel);

        channels.put("id",channel);
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        /*
        该方法会将channelGroup中的所有的channel遍历并发送消息，我们不需要再遍历
         */
        channelGroup.writeAndFlush("[客户端] : " + channel.remoteAddress() + " 离开了[" + LocalDateTime.now().format(formatter) + "]\n");
        System.out.println("channelGroup size : " + channelGroup.size());
    }

    /**
     * 表示channel处于活动状态，提示客户端上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线~");
    }

    /**
     * 表示channel处于不活动的状态，提示客户端离线了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线~");
    }

    /**
     * 读取数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        //获取当前channel
        Channel channel = ctx.channel();
        //这时我们遍历channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(c -> {
            //不是当前的channel，转发消息
            if (channel != c){
                c.writeAndFlush("[客户] : " + channel.remoteAddress() + " 发送了消息 : " + msg + "\n");
            }
            //回显消息给自己
            else {
                c.writeAndFlush("[自己]发送了消息 : " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
