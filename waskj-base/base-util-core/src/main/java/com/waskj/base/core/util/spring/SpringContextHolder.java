package com.waskj.base.core.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * Spring 工具类 ，获取Spring容器中的上下文信息
 * </p
 * <p>
 *  使用的时候需要注入到 servlet-context.xml 如下设置：
 *  <br>
 *  <!-- 获取ApplicationContext上下文 -->
 *	<bean id="springContextHolder" class="com.baomidou.framework.spring.SpringContextHolder" />
 * </p>
 * @author G.Sun
 * @Date 2016-01-15
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return context.getBean(clazz);
    }

    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        context = ac;
    }

    private static void checkApplicationContext() {
        if (context == null) {
            try {
                throw new Exception("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
