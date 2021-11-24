package netty.dubborpc.provider;

import netty.dubborpc.netty.NettyServer;

/**
 * 服务提供方启动服务
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/24 15:58
 */
public class ServerBootstrap {
    public static void main(String[] args){

        NettyServer.startServer("127.0.0.1", 8001);

    }
}
