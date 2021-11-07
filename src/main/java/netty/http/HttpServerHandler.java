package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler<HttpObject>：指定客户端和服务器端的交互的数据类型
 * HttpObject：客户端和服务器端相互通讯的数据被封装成HttpObject
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的子类
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-06 18:13
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        /*
        浏览器请求，会产生两次请求，为什么呢？
        因为浏览器请求的时候有地址请求和图片请求
         */
        //判断msg是不是httpRequest请求
        if (msg instanceof HttpRequest){

            System.out.println("pipeline hashcode : " + ctx.pipeline().hashCode() + " ,HttpServerHandler hashcode : " + this.hashCode());

            System.out.println("msg类型 : " + msg.getClass());
            System.out.println("客户端地址: " + ctx.channel().remoteAddress());

            //解决两次请求问题
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri，过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico ,不做响应");
                return;
            }

            //回复信息给浏览器，http协议
            ByteBuf buf = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            //构造一个http的相应，即HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

            //设置响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buf.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);

        }


    }
}
