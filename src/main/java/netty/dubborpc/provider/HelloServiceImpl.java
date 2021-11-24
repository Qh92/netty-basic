package netty.dubborpc.provider;

import netty.dubborpc.publicinterface.HelloService;

/**
 * 1) dubbo 底层使用了 Netty 作为网络通讯框架，要求用 Netty 实现一个简单的 RPC 框架
 * 2) 模仿 dubbo，消费者和提供者约定接口和协议，消费者远程调用提供者的服务，
 * 提供者返回一个字符串，消费 者打印提供者返回的数据。底层网络通信使用 Netty
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 15:54
 */
public class HelloServiceImpl implements HelloService {


    @Override
    public String hello(String msg) {
        System.out.println("服务提供方收到消费方的消息 : " + msg);
        if (msg != null) {
            return "你好，客户端，服务端已收到消息 : [" + msg +"]";
        }else {
            return "你好，客户端，服务端已收到消息";
        }
    }
}
