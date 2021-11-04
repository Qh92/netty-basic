package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 1）编写一个NIO群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 2）实现多人群聊
 * 3）服务器端：可以监测用户上线，离线，并实现消息转发功能
 * 4）客户端：通过channel可以无阻塞发送消息给其它所有用户，同时可以接受其它用户发送的消息（有服务器转发得到）
 * 5）目的：进一步理解NIO非阻塞网络编程机制
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-04 17:01
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    public GroupChatServer() {

        try {
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(6667));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    private void listen(){
        try {
            //循环处理
            while (true) {
                int count = selector.select();
                //有事件处理
                if (count > 0) {
                    //遍历得到SelectionKey集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = keyIterator.next();

                        //监听到连接
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将该socketChannel注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示该用户已上线
                            System.out.println(socketChannel.getRemoteAddress() + " 上线 ");
                        }
                        //通道发生read事件，即通道是可读的状态
                        else if (key.isReadable()){
                            //处理读
                            readData(key);
                        }
                        //删除当前key,防止重复操作
                        keyIterator.remove();
                    }
                }else {
                    System.out.println("等待....");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }


    /**
     * 读取客户端消息
     * @param key 监听到的事件
     */
    private void readData(SelectionKey key){
        //取到关联的channel
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);

            //根据count的值做处理
            if (count > 0){
                //把缓冲区的数据转成字符串
                String msg = new String(byteBuffer.array());
                //输出该消息
                System.out.println("from 客户端 : " + msg);
                //向其它客户端发送消息（排除自己）
                sendInfoToOtherClients(msg, channel);
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + " 离线 ");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其它客户（通道）
     * @param msg
     * @param self
     */
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的socketChannel，并排除self
        for (SelectionKey key : selector.keys()){
            //通过key取出对应的SocketChannel
            Channel channel =  key.channel();
            //排除自己
            if (channel instanceof SocketChannel && channel != self){
                SocketChannel targetChannel = (SocketChannel)channel;
                //将msg存储到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                targetChannel.write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {

        GroupChatServer server = new GroupChatServer();
        server.listen();

    }
}
