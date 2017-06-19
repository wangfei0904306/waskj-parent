package com.waskj.base.provider.core;

import java.lang.annotation.*;

/**
 * 发送消息注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    /**
     * 发送的消息内容
     * @return
     */
    String msg() default "";

    /**
     * 接收消息的用户组
     * @return
     */
    long[] toRoles() default {};

    /**
     * 接受消息的用户
     * @return
     */
    long[] toUsers() default {};

    /**
     * 点击跳转的页面
     * @return
     */
    String pageUrl() default "";

}
