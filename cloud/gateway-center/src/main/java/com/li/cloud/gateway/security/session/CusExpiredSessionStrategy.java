package com.li.cloud.gateway.security.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @desc 自定义session多设备登录处理策略
 * @date 2020-03-22
 */
public class CusExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

    /** session多设备处理 */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        // 调用父类session失效处理方法
        onSessionInvalid(event.getRequest(), event.getResponse());
    }

    /** 设置session失效原因为并发登录导致 */
    @Override
    protected boolean isConcurrency() {
        return true;
    }
}
