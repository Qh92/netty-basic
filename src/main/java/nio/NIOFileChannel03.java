package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 要求：
 * 1.使用FileChannel和 方法read、write，完成文件的拷贝
 * 2.拷贝一个文本文件1.txt，放在项目下即可
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-01 21:50
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (fileChannel01.read(byteBuffer) != -1){
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
            byteBuffer.clear();
        }

        //循环读取
//        while (true) {
//            //这里有一个重要的操作，一定不要忘了
//            /*
//             public final Buffer clear() {
//                position = 0;
//                limit = capacity;
//                mark = -1;
//                return this;
//            }
//             */
//            //清空buffer
//            byteBuffer.clear();
//            int read = fileChannel01.read(byteBuffer);
//            System.out.println("read =" + read);
//            //表示读完
//            if(read == -1) {
//                break;
//            }
//            //将buffer 中的数据写入到 fileChannel02 -- 2.txt
//            byteBuffer.flip();
//            fileChannel02.write(byteBuffer);
//        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
