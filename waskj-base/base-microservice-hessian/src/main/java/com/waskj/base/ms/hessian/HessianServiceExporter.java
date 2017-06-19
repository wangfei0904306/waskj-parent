package com.waskj.base.ms.hessian;

import com.waskj.base.ms.annotation.ServiceExport;
import com.waskj.base.ms.service.support.AbstractServiceExporter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

public class HessianServiceExporter extends AbstractServiceExporter {
    @Override
    protected Object getExportObject(Class clazz) {
        org.springframework.remoting.caucho.HessianServiceExporter exporter =
                new org.springframework.remoting.caucho.HessianServiceExporter();
        exporter.setServiceInterface(clazz);
        exporter.setService(this.getApplicationContext().getBean(clazz));
        return exporter;
    }

    @Override
    protected String getServiceName(Class clazz) {
        ServiceExport annotation = (ServiceExport)clazz.getAnnotation(ServiceExport.class);
        String serviceName = annotation.name();

        if(!StringUtils.hasText(serviceName)){
            serviceName = clazz.getSimpleName();
        }

        StringBuilder sb = new StringBuilder("/");
        sb.append(serviceName).append("Controller");
        return sb.toString();
    }

    @Override
    protected ConfigurableApplicationContext getApplicationContext() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return (ConfigurableWebApplicationContext) RequestContextUtils.findWebApplicationContext(request);
    }
}
