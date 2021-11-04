package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 分散与聚合
 * scattering:将数据写入到buffer时，可以采用buffer数组，依次写入
 * gathering:从buffer读取数据时，可以采用buffer数组，依次读取
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-03 18:01
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress(9001);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(address);

        //创建buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假定从客户端接收8个字节
        int messageLength = 8;
        //循环读取数据
        while (true){
            int byteRead = 0;
            while (byteRead < messageLength){
                long read = socketChannel.read(buffers);
                //累计读取的字节数
                byteRead += read;
                System.out.println("byteRead : " + byteRead);
                //使用流打印，查看当前的buffer的position,limit
                Arrays.asList(buffers).forEach(b -> System.out.println("positions : " + b.position() + " ,limit : " + b.limit()));
            }

            //将所有的buffer进行flip
            Arrays.asList(buffers).forEach(Buffer::flip);

            //将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength){
                long write = socketChannel.write(buffers);
                byteWrite += write;
            }

            //将所有的buffer进行clear
            Arrays.asList(buffers).forEach(Buffer::clear);

            System.out.println("byteRead : " + byteRead + ", byteWrite : " + byteWrite + ", messageLength : " + messageLength);

        }

    }
}
