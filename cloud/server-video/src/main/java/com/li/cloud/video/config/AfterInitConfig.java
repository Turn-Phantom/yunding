package com.li.cloud.video.config;

import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.video.entity.DbSystemParams;
import com.li.cloud.video.service.DbSystemParamsService;
import com.li.cloud.video.service.VideoListService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @desc 初始化配置完成后执行
 * @date 2020-04-17
 */
@Component
public class AfterInitConfig implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DbSystemParamsService dbSystemParamsService;

    @Autowired
    private YunDingParams yunDingParams;

    @Autowired
    private VideoListService videoListService;

    @Autowired
    private BaseService baseService;

    /** 初始化完成后，加载系统参数 */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询数据库系统参数,并加载到系统内存中
        List<DbSystemParams> dbSystemParams = dbSystemParamsService.queryAllParams();
        Map<String, DbSystemParams> systemParams = yunDingParams.getDbSystemParams();
        systemParams.putAll(dbSystemParams.stream().collect(Collectors.toMap(DbSystemParams::getParKey, obj -> obj)));
        DbSystemParams initVideoList = systemParams.get("initVideoList");
        // 根据系统参数判断，若初始化参数中，未对影片清单初始化，则执行初始化操作
        // 若系统参数为空/参数key不存在/key值为0（0表示未初始化; 1 已初始化）
        if(null == initVideoList || StringUtils.isBlank(initVideoList.getParKey())
                || null == initVideoList.getParState() || 0 == initVideoList.getParState()){
            logger.info("开始汇入影片清单，请稍后 ....");
            boolean isInsert = insertVideoList();
            if(isInsert){
                DbSystemParams sysPar = new DbSystemParams();
                sysPar.setParState(1);
                // 不存在该参数，插入; 否则更新
                if(null == initVideoList || StringUtils.isBlank(initVideoList.getParKey())){
                    sysPar.setParKey("initVideoList");
                    sysPar.setParDesc("影片清单数据初始化；state: 0 未初始化，1 已初始化");
                    baseService.insert(sysPar);
                } else{
                    sysPar.setId(initVideoList.getId());
                    baseService.updateField(sysPar);
                }
            }
        }
    }

    /** 检查是否汇入影片清单数据，若已汇入，则不处理，否则初始化完成后，执行汇入操作 */
    public boolean insertVideoList(){
        int insertNum = videoListService.insertVideoList();
        if(insertNum <= 0){
            logger.error("汇入影片清单数据失败！");
            return false;
        }
        logger.info("汇入影片清单数据成功，汇入结果：" + insertNum);
        return true;
    }

    /** RestTemplate */
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                .setMaxConnTotal(3000) // 最大连接数
                .setMaxConnPerRoute(500) // 单个路由连接的最大数
                .build());
        httpRequestFactory.setConnectionRequestTimeout(40000);
        httpRequestFactory.setConnectTimeout(40000);
        httpRequestFactory.setReadTimeout(40000);
        return new RestTemplate(httpRequestFactory);
    }
}
