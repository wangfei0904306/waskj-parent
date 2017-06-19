package com.waskj.base.ms.spring;

import com.waskj.base.ms.annotation.ServiceExport;
import com.waskj.base.ms.annotation.ServiceImport;
import com.waskj.base.ms.api.ServiceRegistrationService;
import com.waskj.base.ms.config.ServiceConfig;
import com.waskj.base.ms.service.ServiceExporter;
import com.waskj.base.ms.service.ServiceProxy;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 在启动时注册服务
 */
public class MicroServiceStartupRegistration implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MicroServiceStartupRegistration.class);

    private ServiceConfig config;

    private ServiceRegistrationService registrationService;

    private ServiceExporter serviceExporter;

    private ServiceProxy serviceProxy;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if( event.getApplicationContext().getParent() == null ) {
            // 1. 注册服务
            registrationService.doRegistration(config);

            // 2. 暴露本地服务
            Set<Class> exportClass = getInterfacesByAnnotation(config.getExportPackage(), ServiceExport.class);
            for (Class clazz : exportClass) {
                this.serviceExporter.exportServices(clazz);
            }

            // 3. 注入本地服务代理
            Set<Class> proxyClass = getInterfacesByAnnotation(config.getExportPackage(), ServiceImport.class);
            for (Class clazz : proxyClass) {
                this.serviceProxy.proxyTargetService(clazz);
            }
        }
    }

    public ServiceConfig getConfig() {
        return config;
    }

    public void setConfig(ServiceConfig config) {
        this.config = config;
    }

    public ServiceRegistrationService getRegistrationService() {
        return registrationService;
    }

    public void setRegistrationService(ServiceRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public ServiceExporter getServiceExporter() {
        return serviceExporter;
    }

    public void setServiceExporter(ServiceExporter serviceExporter) {
        this.serviceExporter = serviceExporter;
    }

    public ServiceProxy getServiceProxy() {
        return serviceProxy;
    }

    public void setServiceProxy(ServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    /**
     * 根据包名，获取指定注解的接口
     * @param basePackage
     * @param annoClazz
     * @return
     */
    private Set<Class> getInterfacesByAnnotation(String basePackage,Class annoClazz){
        Set<Class> retSet = new HashSet<>();

        PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = scanner.getResources(basePackage);

        } catch (IOException e) {
            logger.error("find class in package " + basePackage + " error",e);
        }
        
        if( resources != null ){
            for (Resource resource : resources) {
                Class clazz = this.getClassFromResource(resource);
                if( clazz == null || !clazz.isAnnotationPresent(annoClazz) ){
                    continue;
                }
                retSet.add(clazz);
            }
        }
        return retSet;
    }

    private Class getClassFromResource(Resource resource){
        Class ret = null;
        try {
            String resourceUri = resource.getURI().toString();
            resourceUri = resourceUri.replace(".class","").replace("/",".");
            ret = Class.forName(resourceUri);
        } catch (IOException e) {
            logger.error("get class from resource error",e);
        } catch (ClassNotFoundException e) {
            logger.error("convert class by uri error",e);
        }
        return ret;
    }
}
