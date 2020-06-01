package top.yms;

import top.yms.rpctools.RpcRequest;
import top.yms.rpctools.RpcResponse;

public interface RpcClient {
    /**
     * 发起请求，获取响应
     * @param request
     * @return
     */
    RpcResponse sendRequest(RpcRequest request) throws Exception;
}
