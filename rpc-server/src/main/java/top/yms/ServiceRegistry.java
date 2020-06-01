package top.yms;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(
            ServiceRegistry.class);
    private static final Map<String, Object> registeredServices =
            new HashMap<>();
    public static <T> T getService(String className) {
        return (T) registeredServices.get(className);
    }
    public static void registerService(Class<?> interfaceClass,
                                       Class<?> implClass) {
        try {
            registeredServices.put(interfaceClass.getName(), implClass.newInstance());
            logger.info("服务注册成功,接口：{},实现{}", interfaceClass.getName(), implClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务" + implClass + "注册失败", e);
        }
    }
}
