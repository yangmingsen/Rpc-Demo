package top.yms.test;

import top.yms.BioRpcServer;
import top.yms.ServiceRegistry;
import top.yms.service.HelloService;
import top.yms.service.HelloServiceImpl;

public class RpcServerTest {


    public static void main(String[] args) {
        // 注册服务
        ServiceRegistry.registerService(HelloService.class, HelloServiceImpl.class);
// 启动服务
        new BioRpcServer(9000).start();

    }
}
