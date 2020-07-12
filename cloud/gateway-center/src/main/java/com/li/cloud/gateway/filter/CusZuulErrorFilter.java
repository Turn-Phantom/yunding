package com.li.cloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;

/**
 * @desc 自定义网关错误过滤器
 * @date 2020-05-20
 */
@Component
public class CusZuulErrorFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.error("错误过滤器执行");
        RequestContext requestContext = RequestContext.getCurrentContext();
        System.out.println("错误异常返回：" + requestContext.getResponseBody());
        Object e = requestContext.get("throwable");
        ZuulException zuulException = (ZuulException) e;
        zuulException.printStackTrace();
        return null;
    }
}
