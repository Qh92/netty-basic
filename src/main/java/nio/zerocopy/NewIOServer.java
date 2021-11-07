package nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


/**
 * 零拷贝基本介绍
 * 1）零拷贝是网络编程的关键，很多性能优化都离不开
 * 2）在java程序中，常用的零拷贝有mmap(内存映射)和sendFile
 *
 * 零拷贝的再次理解
 * 1）零拷贝，是从操作系统的角度来说的。因为内核缓冲区之间，没有数据是重复的（只有kernel buffer有一份数据）。
 * 2）零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，例如更少的上下文切换，更少的CPU缓存伪共享以及无CPU校验和计算
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-04 17:01
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {

        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();

        serverSocket.bind(address);

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readcount = 0;
            while (-1 != readcount) {
                try {

                    readcount = socketChannel.read(byteBuffer);

                }catch (Exception ex) {
                   // ex.printStackTrace();
                    break;
                }
                //
                byteBuffer.rewind(); //倒带 position = 0 mark 作废
            }
        }
    }
}
