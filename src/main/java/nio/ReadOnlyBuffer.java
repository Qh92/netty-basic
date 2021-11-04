package nio;

import java.nio.ByteBuffer;

/**
 * 可以将一个普通Buffer转成只读Buffer
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-03 16:19
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        //读取
        byteBuffer.flip();

        //得到一个只读的Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        //读取
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
        //java.nio.ReadOnlyBufferException
        readOnlyBuffer.put((byte) 24);
    }
}
