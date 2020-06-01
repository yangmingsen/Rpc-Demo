package top.yms;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yms.rpctools.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioRpcServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(
            BioRpcServer.class);
    // 用来处理请求的连接池
    private static final ExecutorService es = Executors.newCachedThreadPool();
    private int port = 9000;// 默认端口
    private volatile boolean shutdown = false;// 是否停止

    /**
     * 使用默认端口9000，构建一个BIO的RPC服务端
     */
    public BioRpcServer() {
    }

    /**
     * 使用指定端口构建一个BIO的RPC服务端
     *
     * @param port 服务端端口
     */
    public BioRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try {
// 启动服务
            ServerSocket server = new ServerSocket(this.port);
            logger.info("服务启动成功，端口：{}", this.port);
            while (!this.shutdown) {
// 接收客户端请求
                Socket client = server.accept();
                es.execute(() -> {
                    try (
// 使用JDK的序列化流
                            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())
                    ) {
// 读取请求参数
                        RpcRequest request = (RpcRequest) in.readObject();
                        logger.info("接收请求，{}.{}({})",
                                request.getClassName(),
                                request.getMethodName(),
                                StringUtils.join(request.getParameterTypes(), ", "));
                        logger.info("请求参数：{}",
                                StringUtils.join(request.getParameters(), ", "));
// 处理请求
                        out.writeObject(RequestHandler.handleRequest(request));
                    } catch (Exception e) {
                        logger.error("客户端连接异常，客户端{}:{}",
                                client.getInetAddress().toString());
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("服务启动失败", e);
        }
    }

    @Override
    public void stop() {
        this.shutdown = true;
        logger.info("服务即将停止");
    }
}
