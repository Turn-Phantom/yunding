package com.online.yunding.common.spring;

import com.alibaba.fastjson.JSONObject;
import com.online.yunding.common.basecurd.entity.Pagination;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @desc: 自定义切面通知
 * @date: 2019-10-30
 */
@Aspect
@Configuration
public class CustomAspect {
    /**
     * @desc: 前置处理
     * @param: point 方法参数集合
     * @date: 2019-10-31
     */
    @Before("@annotation(com.online.yunding.common.annotations.CustomPage)") // 处理含有CustomPage注解的方法
    public void paginationBefore(JoinPoint point){
        Pagination pagination = (Pagination)point.getArgs()[0];
        // 设置分页参数
        pagination.initPageHelper();
    }

    /**
     * @desc: 方法返回时执切面执行
     *      execution 指定切点为com.blank.sky.service.Impl包下的所有方法，
     *      @annotation 指定含有CustomPage注解的类，或方法
     * @param: point
     * @param: returnValue
     * @date: 2019-10-31
     */
    @AfterReturning(returning="returnValue", pointcut = "execution(public * com.online.yunding.service..*.*(..))&&@annotation(com.online.yunding.common.annotations.CustomPage)")
    public void processPagination(JoinPoint point, Object returnValue){
        Pagination pagination = (Pagination)point.getArgs()[0];
        // 通过返回值截取分页查询相关数据，并设置分页总条数;(分页信息以及分页数据同时返回，需要做处理)
        String resStr = returnValue.toString();
        String pageStr = resStr.substring(resStr.indexOf("{"), resStr.indexOf("}") + 1);
        pageStr = pageStr.replace("=", ":");
        JSONObject jsonObject = JSONObject.parseObject(pageStr);
        pagination.setResultTotal((int)jsonObject.get("total"));
        // 将数据转换为返回结果数据
        List<Object> returnValue1 = (List<Object>) returnValue;
        pagination.setResultDatas(returnValue1);
    }
}
