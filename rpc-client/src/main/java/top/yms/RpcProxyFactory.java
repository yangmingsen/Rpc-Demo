package top.yms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yms.rpctools.RpcRequest;
import top.yms.rpctools.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxyFactory<T> implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcProxyFactory.class);

    private Class<T> clazz;

    public RpcProxyFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getProxyObject() {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 处理Object中的方法
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        // 封装请求参数
        RpcRequest request = new RpcRequest();
        request.setClassName(clazz.getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());

        try {
            // 发起网络请求,并接收响应
            RpcClient client = new BioRpcClient("127.0.0.1", 9000);
            RpcResponse response = client.sendRequest(request);

            // 解析并返回
            if (response.getStatus() == 1) {
                logger.info("调用远程服务成功！");
                return response.getData();
            }

            logger.debug("远程服务调用失败，{}。", response.getError());
            return null;
        } catch (Exception e) {
            logger.error("远程调用异常", e);
            throw new RuntimeException(e);
        }
    }
}
