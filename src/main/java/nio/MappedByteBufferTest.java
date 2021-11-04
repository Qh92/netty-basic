package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-03 16:33
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");

        FileChannel accessFileChannel = randomAccessFile.getChannel();

        /*
        参数一：使用的是读写模式
        参数二：可以直接修改的起始位置
        参数三：映射到内存的大小（不是索引位置），即将文件1.txt的多少个字节映射到内存，可以直接修改的范围就是0~5

        实际类型为DirectByteBuffer
         */
        MappedByteBuffer map = accessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte)'H');
        map.put(3,(byte) 9);
        //java.lang.IndexOutOfBoundsException
        //map.put(5,(byte) 'Y');

        randomAccessFile.close();


    }
}
