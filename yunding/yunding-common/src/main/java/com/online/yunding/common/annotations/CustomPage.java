package com.online.yunding.common.annotations;

import java.lang.annotation.*;

/**
 * @desc: 自定义分页处理
 * @author: lxs
 * @date: 2019-10-30
 */
@Target(value = ElementType.METHOD) // 声明该注解只能使用在方法上
@Retention(RetentionPolicy.RUNTIME) // 声明该注解允许在运行阶段加载
@Documented // 指定注解可生成到javaDoc文档中
public @interface CustomPage {

}
