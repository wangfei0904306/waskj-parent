package com.waskj.base.ms.service.support;

import com.waskj.base.ms.annotation.ServiceExport;
import com.waskj.base.ms.service.ServiceExporter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 */
public abstract class AbstractServiceExporter implements ServiceExporter {

    @Override
    public void exportServices(Class clazz) {
        // check the class
        if (!this.canExport(clazz)) {
            return;
        }
        Object exporter = this.getExportObject(clazz);
        this.registerBean(this.getServiceName(clazz),exporter);
    }

    /**
     * 检查Class是否可以导出
     * @param clazz
     * @return
     */
    protected boolean canExport(Class clazz){
        return clazz.isAnnotationPresent(ServiceExport.class);
    }

    protected void registerBean(String serviceName,Object bean){
        getApplicationContext().getBeanFactory().registerSingleton(serviceName,bean);
    }

    protected abstract ConfigurableApplicationContext getApplicationContext();

    protected abstract Object getExportObject(Class clazz);

    /**
     * 获取服务名称
     * @param clazz
     * @return
     */
    protected abstract String getServiceName(Class clazz);
}
