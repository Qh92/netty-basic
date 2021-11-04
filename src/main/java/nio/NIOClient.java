package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-04 11:40
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        //连接服务器
        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1",6666))){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它操作");
            }
        }
        //如果连接成功，就发送数据
        String data = "国王排名";
        ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        //代码阻塞在这
        System.in.read();
    }

}
