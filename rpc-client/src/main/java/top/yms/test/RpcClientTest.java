package top.yms.test;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import top.yms.RpcProxyFactory;
import top.yms.service.HelloService;

public class RpcClientTest {
    public static void main(String[] args) {
        // 通过代理工厂，获取服务
        HelloService helloService = new RpcProxyFactory<>(HelloService.class).getProxyObject();
// 调用服务
        String result = helloService.sayHello("Jack");
        System.out.println(result);
//        Assert.assertEquals("调用失败", "hello, Jack", result);
    }

}
