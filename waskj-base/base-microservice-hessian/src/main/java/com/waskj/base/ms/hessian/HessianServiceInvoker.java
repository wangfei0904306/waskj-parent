package com.waskj.base.ms.hessian;


import com.caucho.hessian.client.HessianProxyFactory;
import com.waskj.base.ms.annotation.ServiceExport;
import com.waskj.base.ms.annotation.ServiceImport;
import com.waskj.base.ms.api.ServiceDiscoveryService;
import com.waskj.base.ms.dto.DiscoveryResult;
import com.waskj.base.ms.service.ServiceInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class HessianServiceInvoker implements ServiceInvoker,MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(HessianServiceInvoker.class);

    private ServiceDiscoveryService discoveryService;

    private HessianProxyFactory proxyFactory = new HessianProxyFactory();

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        ServiceImport anno =  o.getClass().getAnnotation(ServiceImport.class);
        ServiceExport exportAnno = o.getClass().getAnnotation(ServiceExport.class);

        String serviceName = anno.name();

        DiscoveryResult result = discoveryService.discoveryService(serviceName);

        if(!result.getFounded()){
            // 没有找到服务
            return null;
        }

        String ip = result.getHost();
        Integer port = result.getPort();
        String serviceApi = null;
        if( exportAnno != null && StringUtils.hasText(exportAnno.name())){
            serviceApi = exportAnno.name();
        } else {
            serviceApi = o.getClass().getSimpleName();
        }

        StringBuilder url = new StringBuilder("http://");
        url.append(ip).append(":").append(port).append("/").append(serviceApi);

        proxyFactory.create(o.getClass(),url.toString());


        return null;
    }

    public ServiceDiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    public void setDiscoveryService(ServiceDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }
}
