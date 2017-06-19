package com.waskj.base.ms.api;

import com.waskj.base.ms.dto.DiscoveryResult;

/**
 * 服务发现接口
 * @author 徐成龙
 */
public interface ServiceDiscoveryService {

    DiscoveryResult discoveryService(String serviceName);

}
