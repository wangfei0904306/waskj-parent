package com.waskj.base.ms.service;

/**
 * 服务代理
 * 初步思路，打算使用cglib代理target service
 *
 */
public interface ServiceProxy {

    /**
     * 代理目标Class
     * 判断目标服务是否已经注册( ApplicationContext.getBean(Class) )
     * 没有注册，使用cglib进行代理
     *      代理方式：
     *             获取 服务名称，从 Eureka 中获取 host port
     *             拼接 url，访问 remote service( Service Invoker )
     * @return
     */
    void proxyTargetService(Class clazz);

}
