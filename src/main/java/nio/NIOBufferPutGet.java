package nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer支持类型化的put和get,put放入的是什么数据类型，get就应该使用相应的数据类型来取出
 * 否则可能有BufferUnderflowException异常
 *
 * @author Qh
 * @version 1.0
 * @date 2021-11-03 16:09
 */
public class NIOBufferPutGet {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //类型化方式放入数据
        byteBuffer.putInt(10000);
        byteBuffer.putLong(9);
        byteBuffer.putChar('秦');
        byteBuffer.putShort((short) 4);

        //取出
        byteBuffer.flip();

        System.out.println();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}
