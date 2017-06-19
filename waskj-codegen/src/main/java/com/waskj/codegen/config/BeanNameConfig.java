package com.waskj.codegen.config;

/**
 * Created by poet on 2016/10/27.
 */
public class BeanNameConfig {

    private String domainSuffix;

    private String daoSuffix;

    private String serviceSuffix;

    private String serviceImplSuffix;

    private String controllerSuffix;

    public void setDomainSuffix(String domainSuffix) {
        this.domainSuffix = domainSuffix;
    }

    public String getDomainSuffix() {
        return domainSuffix;
}

    public String getDaoSuffix() {
        return daoSuffix;
    }

    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getServiceImplSuffix() {
        return serviceImplSuffix;
    }

    public void setServiceImplSuffix(String serviceImplSuffix) {
        this.serviceImplSuffix = serviceImplSuffix;
    }

    public String getControllerSuffix() {
        return controllerSuffix;
    }

    public void setControllerSuffix(String controllerSuffix) {
        this.controllerSuffix = controllerSuffix;
    }
}
