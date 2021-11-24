/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package netty.source.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /** 充当业务线程池，可以将任务提交到该线程池中，初始化16个线程 */
    private static EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println("EchoServerHandler 当前线程名称 : " + Thread.currentThread().getName());

        /*//将任务提交到group线程池中
        group.submit(() -> {
            //接收客户端消息
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String message = new String(bytes, CharsetUtil.UTF_8);
            //模拟耗时操作
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("group submit 执行的线程 : " + Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
        });
        group.submit(() -> {
            //接收客户端消息
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String message = new String(bytes, CharsetUtil.UTF_8);
            //模拟耗时操作
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("group submit 执行的线程 : " + Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
        });*/

        /*try {
            TimeUnit.SECONDS.sleep(10);

            System.out.println("EchoServerHandler 当前线程名称 : " + Thread.currentThread().getName());

            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //模拟耗时操作，以下都是同一个线程执行
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);

                System.out.println("EchoServerHandler#execute1 当前线程名称 : " + Thread.currentThread().getName());

                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);

                System.out.println("EchoServerHandler#execute2 当前线程名称 : " + Thread.currentThread().getName());

                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,这里模拟一个耗时业务1~", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("继续.........");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
