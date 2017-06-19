package com.waskj.base.ms.api;

/**
 * 更新服务状态
 */
public interface ServiceStatusService {

    /**
     * 服务启动中
     */
    void statusStarting();

    /**
     * 服务已经启动
     */
    void statusUp();

    /**
     * 服务Down
     */
    void statusDown();

    /**
     * 服务未知
     */
    void statusUnknow();

    /**
     * 服务不可用
     */
    void statusOutOfService();

}
