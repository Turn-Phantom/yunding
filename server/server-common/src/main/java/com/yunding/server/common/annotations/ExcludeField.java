package com.yunding.server.common.annotations;

import java.lang.annotation.*;

/**
 * @desc: 排除字段注解，排除不需要处理的字段
 * @date: 2019-10-06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcludeField {
}
