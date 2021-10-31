package bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-10-31 21:50
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动");

        while (true){
            System.out.println("服务器等待连接...");
            //阻塞
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            newCachedThreadPool.execute(() -> handler(socket));
        }
    }

    private static void handler(Socket socket) {

        try {
            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环的读取客户端发送的数据
            while (true) {

                System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());

                System.out.println("read....");
                System.out.println("数据获取....");
                int read;
                //read 阻塞
                if((read = inputStream.read(bytes)) != -1) {
                    //输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
