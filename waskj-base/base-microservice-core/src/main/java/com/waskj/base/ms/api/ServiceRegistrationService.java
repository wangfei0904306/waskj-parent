package com.waskj.base.ms.api;

import com.waskj.base.ms.config.ServiceConfig;

/**
 * 服务注册接口
 * @author 徐成龙
 */
public interface ServiceRegistrationService {

    void doRegistration(ServiceConfig config);

}
