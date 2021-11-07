package netty.simple;

import io.netty.util.NettyRuntime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-11-06 14:18
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
