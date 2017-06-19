package com.waskj.base.ms.service;

/**
 * 将被代理的Service导出
 */
public interface ServiceExporter {

    /**
     * 导出服务
     * 导出方式
     *      1. rpc
     *      2. rest
     *      3. 手动注册spring mvc handler adapter
     * @param clazz
     */
    void exportServices(Class clazz);

}
