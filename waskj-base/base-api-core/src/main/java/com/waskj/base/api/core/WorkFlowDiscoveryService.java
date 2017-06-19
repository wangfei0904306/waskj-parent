package com.waskj.base.api.core;

/**
 * 工作流回调Service发现
 */
public interface WorkFlowDiscoveryService {

    <T> WorkFlowCallBackService<T> discoveryService(Class<T> entityClass);

}
