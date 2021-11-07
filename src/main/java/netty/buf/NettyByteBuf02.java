package netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-07 20:26
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("hello world,成都", CharsetUtil.UTF_8);

        //使用相关的方法
        //true
        if (buf.hasArray()){
            byte[] content = buf.array();
            //将content转成字符串
            System.out.println(new String(content,CharsetUtil.UTF_8));
            System.out.println("buf : " + buf);
            //数组偏移量
            System.out.println(buf.arrayOffset());
            //读取的下标
            System.out.println(buf.readerIndex());
            //写入的下标
            System.out.println(buf.writerIndex());
            //buf容量
            System.out.println(buf.capacity());

            //可读取的字节数
            int len = buf.readableBytes();
            System.out.println("len : " + len);

            //取出各个字节
            for (int i = 0; i < len; i++) {
                System.out.println((char) buf.getByte(i));
            }
            //从索引为0的位置读取4个字节
            System.out.println(buf.getCharSequence(0, 4, CharsetUtil.UTF_8));
        }

    }
}
