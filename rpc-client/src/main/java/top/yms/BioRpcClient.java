package top.yms;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yms.rpctools.RpcRequest;
import top.yms.rpctools.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BioRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(BioRpcClient.class);

    private String host;
    private int port;

    public BioRpcClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        try (
                Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            logger.info("建立连接成功：{}：{}", host, port);
// 发起请求
            out.writeObject(request);
            logger.info("发起请求,目标主机{}:{}，服务：{}.{}({})", host, port,
                    request.getClassName(), request.getMethodName(),
                    StringUtils.join(request.getParameterTypes(), ","));
// 获取结果
            return (RpcResponse) in.readObject();
        }
    }
}
