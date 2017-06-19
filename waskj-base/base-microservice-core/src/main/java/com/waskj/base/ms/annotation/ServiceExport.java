package com.waskj.base.ms.annotation;

import java.lang.annotation.*;

/**
 * 服务暴露，用于注解在Service接口上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceExport {

    /**
     * 接口对应暴露的名称
     * @return
     */
    String name() default "";

}
