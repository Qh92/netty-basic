package netty.dubborpc.consumer;

import netty.dubborpc.netty.NettyClient;
import netty.dubborpc.provider.HelloServiceImpl;
import netty.dubborpc.publicinterface.HelloService;

/**
 * 客户端启动器
 * 
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 17:26
 */
public class ClientBootstrap {

    public static final String PROTOCOL = "helloService#hello#";

    public static void main(String[] args){

        NettyClient consumer = new NettyClient();
        //创建代理对象
        HelloService service = (HelloService)consumer.getBean(HelloService.class, PROTOCOL);

        String result = service.hello("你好,dubbo.....");

        System.out.println("调用的结果 : " + result);

    }
}
