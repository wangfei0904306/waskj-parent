package com.waskj.base.ms.annotation;

import java.lang.annotation.*;

/**
 * 服务引用，用于注解在Service接口上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceImport {

    /**
     * 要引入的服务名称
     * @return
     */
    String name() default "";

}
