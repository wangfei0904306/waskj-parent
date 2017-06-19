package com.waskj.base.ms.hessian;


import com.caucho.hessian.client.HessianProxyFactory;
import com.waskj.base.ms.annotation.ServiceExport;
import com.waskj.base.ms.annotation.ServiceImport;
import com.waskj.base.ms.api.ServiceDiscoveryService;
import com.waskj.base.ms.dto.DiscoveryResult;
import com.waskj.base.ms.service.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class HessianServiceProxy implements ServiceProxy {

    private final Logger logger = LoggerFactory.getLogger(HessianServiceProxy.class);

    private ServiceDiscoveryService discoveryService;

    private HessianProxyFactory proxyFactory;

    @Override
    public void proxyTargetService(Class clazz) {

        // 检查class 是否被ServiceImport所注解
        if(!clazz.isAnnotationPresent(ServiceImport.class)){
            return;
        }

        // 检查clazz是否已经被注册过实现类
        if (getApplicationContext().getBean(clazz) != null) {
            return ;
        }

        // 动态代理clazz
        ServiceImport anno =  (ServiceImport)clazz.getAnnotation(ServiceImport.class);
        ServiceExport exportAnno = (ServiceExport)clazz.getAnnotation(ServiceExport.class);
        String serviceName = anno.name();
        DiscoveryResult result = discoveryService.discoveryService(serviceName);

        if(!result.getFounded()){
            // 没有找到服务
            return;
        }

        String ip = result.getHost();
        Integer port = result.getPort();
        String serviceApi = null;
        if( exportAnno != null && StringUtils.hasText(exportAnno.name())){
            serviceApi = exportAnno.name();
        } else {
            serviceApi = clazz.getSimpleName();
        }
        // 注册到Spring context(root context)中
        StringBuilder url = new StringBuilder("http://");
        url.append(ip).append(":").append(port).append("/").append(serviceApi);

        Object proxy = null;
        try {
            proxy = proxyFactory.create(clazz,url.toString());
        } catch (MalformedURLException e) {
            logger.error("proxy target class " + clazz.getName() + " with url: " + url + " error!",e);
        }

//        proxy.

    }

    protected ConfigurableApplicationContext getApplicationContext() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (ConfigurableWebApplicationContext) RequestContextUtils.findWebApplicationContext(request);
    }

    public ServiceDiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    public void setDiscoveryService(ServiceDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }
}
