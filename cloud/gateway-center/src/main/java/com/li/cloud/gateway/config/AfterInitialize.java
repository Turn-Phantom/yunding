package com.li.cloud.gateway.config;

        import com.li.cloud.common.utils.InterAddressUtils;
        import com.li.cloud.gateway.config.params.SpringConfig;
        import org.apache.commons.lang3.StringUtils;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.InitializingBean;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.cloud.client.ServiceInstance;
        import org.springframework.cloud.client.discovery.DiscoveryClient;
        import org.springframework.cloud.client.loadbalancer.LoadBalanced;
        import org.springframework.context.annotation.Bean;
        import org.springframework.stereotype.Component;
        import org.springframework.web.client.RestTemplate;

        import java.util.List;
        import java.util.Map;

/**
 * @desc 初始化配置文件后执行
 * @date 2020-05-23
 */
@Component
public class AfterInitialize implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpringConfig springConfig;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查redis服务
        checkRedisServer();

        // 检查第三方接口管理服务是否注册并启动
        checkThirdServer();
    }

    /** 检查redis服务 */
    private void checkRedisServer(){
        Map<String, String> redisConf = springConfig.getRedis();
        boolean isEnable = InterAddressUtils.isEnablePort(redisConf, "redis服务");
        if(!isEnable){
            System.exit(1);
        }
        logger.info("检查 1 ===========>  redis 服务启动正常");
    }

    /** 检查第三方接口管理服务是否注册并启动 */
    private void checkThirdServer(){
        boolean serverEnabled = false;
        // 检查第三方接口管理服务是否启动
        List<ServiceInstance> instances = discoveryClient.getInstances("THIRD-CENTER");
        if(null == instances || instances.size() == 0){
            logger.error("启动失败：第三方接口管理服务未注册");
            System.exit(1);
        }
        for (ServiceInstance instance : instances) {
            boolean isEnable = InterAddressUtils.isEnablePort(instance.getHost(), instance.getPort());
            if(isEnable){
                serverEnabled = true;
                break;
            }
        }
        // 若服务都不可用，则停止启动
        if(!serverEnabled){
            logger.error("启动失败：第三方接口管理服务未启动");
            System.exit(1);
        }
        logger.info("检查 2 ===========>  第三方接口管理服务启动正常");
    }

    /** 使用负载均衡的请求对象 */
    @Bean(name = "restTemplateForBalance")
    @LoadBalanced
    public RestTemplate restTemplateForBalance(){
        return new RestTemplate();
    }
}
