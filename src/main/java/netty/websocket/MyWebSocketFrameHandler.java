package netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TextWebSocketFrame 类型：表示一个文本帧（frame）
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-09 20:34
 */
public class MyWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息 : " + msg.text());
        //回复浏览器消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 : " + LocalDateTime.now() + " ,已发送消息 : " + msg.text()));
    }


    /**
     * 当web客户端连接后，触发方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值，LongText是唯一的，ShortText不是唯一的
        System.out.println("handlerAdded 被调用 : " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 : " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用 : " + ctx.channel().id().asLongText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发送 : " + cause.getMessage());
        ctx.close();
    }
}
