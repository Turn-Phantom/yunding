package com.li.cloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @desc 自定义网关过滤器
 * @date 2020-05-19
 */
@Component
public class CusZuulPreFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 过滤器类型；决定过滤器在请求的哪个生命周期中执行
     *      per: 表示在路由之前执行
     *      routing：路由请求时被调用
     *      post：routing和error过滤器之后被调用
     *      error：处理请求发生错误时被调用
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**  过滤器执行顺序；当存在多个过滤器时，可通过该方法指定顺序；值越小，优先级越高 */
    @Override
    public int filterOrder() {
        return 0;
    }

    /** 判断该过滤器是否需要被执行；可指定过滤器的有效范围 */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /** 过滤器的具体逻辑 */
    @Override
    public Object run() throws ZuulException {
        // 判断请求地址是否为开放性请求地址
        RequestContext requestContext = RequestContext.getCurrentContext();
        return null;
    }

}
