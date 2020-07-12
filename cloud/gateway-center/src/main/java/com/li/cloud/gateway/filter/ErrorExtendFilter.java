package com.li.cloud.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

/**
 * @desc 错误过滤器拓展
 *  在zuul执行链中 当post过滤器抛出异常时，error过滤器在post过滤器之前，所以需要额外扩展
 * @date 2020-05-20
 */
//@Component
public class ErrorExtendFilter extends SendErrorFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return super.filterType();
    }

    @Override
    public int filterOrder() {
        return 30;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logger.error("错误拓展过滤器执行");
        return super.run();
    }
}
