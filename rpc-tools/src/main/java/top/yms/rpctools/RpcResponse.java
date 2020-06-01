package top.yms.rpctools;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 2L;
    private int status;// 响应状态 0失败，1成功
    private String error;// 错误信息
    private Object data;// 返回结果数据
    public static RpcResponse ok(Object data) {
        return build(1, null, data);
    }

    public static RpcResponse error(String error) {
        return build(0, error, null);
    }
    public static RpcResponse build(int status, String error, Object data) {
        RpcResponse r = new RpcResponse();
        r.setStatus(status);
        r.setError(error);
        r.setData(data);
        return r;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
