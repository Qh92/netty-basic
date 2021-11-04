package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 目的：理解NIO非阻塞网络编程机制
 *
 * selector.keys():channel总共注册了哪些事件
 * selector.selectedKeys()：channel正在发生的事件有哪些
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-04 10:58
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定一个6666端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到selector,关心连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionKey 数量 : " + selector.keys().size());

        //循环等待客户端连接
        while (true) {
            //没有事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务器端等待1s,无连接");
                continue;
            }
            /*
            如果返回的大于0,就获取到相关的selectionKey集合
            1.如果返回的>0，表示已经获取到关注的事件
            2.selector.selectedKeys() 返回关注事件的集合
            3.通过selectionKeys 反向获取通道
            selectedKeys()：从内部集合中得到所有的SelectionKey
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectionKey 数量 : " + selectionKeys.size());

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应处理，如果是OP_ACCEPT,有新的客户端连接
                if (key.isAcceptable()){
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功，生成一个socketChannel " + socketChannel.hashCode());
                    /*
                    将socketChannel注册到selector,关注事件为OP_READ,同时给socketChannel关联一个Buffer
                     */
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后，注册的selectionKey 数量 : " + selector.keys().size());
                }
                //发生OP_READ事件
                else if (key.isReadable()) {
                    //通过key反向获取到对应的channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("客户端发送数据 : " + new String(byteBuffer.array()));
                }
                //从集合中移除当前的SelectionKey,防止重复操作
                keyIterator.remove();
            }

        }
    }
}
