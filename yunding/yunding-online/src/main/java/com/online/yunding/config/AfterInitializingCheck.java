package com.online.yunding.config;

import com.online.yunding.common.utils.InterAddressUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @desc 参数初始化完成后检查
 * @date 2020-04-08
 */
@Component
public class AfterInitializingCheck implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigParams configParams;

    @Autowired
    private SpringConfigParams springConfigParams;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查队列服务
        checkQueueServer();
        // 检查redis服务
        checkRedisServer();
        // 检查视频服务
        checkVideoServer();

//        logger.info("系统启动成功！");
    }

    /** 检查redis服务 */
    private void checkRedisServer(){
        Map<String, String> redisConf = springConfigParams.getRedis();
        if(null == redisConf){
            logger.error("启动失败：未配置redis服务地址信息");
            System.exit(1);
        }
        String host = redisConf.get("host");
        String port = redisConf.get("port");
        if(StringUtils.isEmpty(host)){
            logger.error("启动失败：未配置redis服务主机地址");
            System.exit(1);
        }
        if(StringUtils.isEmpty(port)){
            logger.error("启动失败：未配置redis服务主机端口");
            System.exit(1);
        }
        boolean enablePort = InterAddressUtils.isEnablePort(host, Integer.valueOf(port));
        if(!enablePort){
            logger.error("启动失败：无法访问redis服务");
            System.exit(1);
        }
    }

    /** 检查队列服务 */
    private void checkQueueServer(){
        ServerObj queueAddr = configParams.getQueueServer();
        if(null == queueAddr){
            logger.error("启动失败：未配置队列服务地址信息");
            System.exit(1);
        }
        String host = queueAddr.getHost();
        Integer port = queueAddr.getPort();
        if(StringUtils.isEmpty(host)){
            logger.error("启动失败：未配置队列服务主机地址");
            System.exit(1);
        }
        if(null == port){
            logger.error("启动失败：未配置队列服务主机端口");
            System.exit(1);
        }
        String realHost = host.replace("http://","").replace("https://", "");
        boolean enablePort = InterAddressUtils.isEnablePort(realHost, port);
        if(!enablePort){
            logger.error("启动失败：无法访问队列服务");
            System.exit(1);
        }
    }

    /** 检查视频服务地址 */
    public void checkVideoServer(){
        ServerObj videoServer = configParams.getVideoServer();
        if(null == videoServer){
            logger.error("启动失败：未配置视频服务地址信息");
            System.exit(1);
        }
        String host = videoServer.getHost();
        Integer port = videoServer.getPort();
        if(StringUtils.isEmpty(host)){
            logger.error("启动失败：未配置视频服务主机地址");
            System.exit(1);
        }
        if(null == port){
            logger.error("启动失败：未配置视频服务主机端口");
            System.exit(1);
        }
        String realHost = host.replace("http://","").replace("https://", "");
        boolean enablePort = InterAddressUtils.isEnablePort(realHost, port);
        if(!enablePort){
            logger.error("启动失败：无法访问视频服务");
            System.exit(1);
        }
    }


}
