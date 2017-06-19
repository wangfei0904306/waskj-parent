package com.waskj.codegen.config;

/**
 * Created by poet on 2016/10/27.
 */
public class PackageConfig {

    private String domainBasePackage;

    private String serviceBasePackage;

    private String serviceImplBasePackage;

    private String daoBasePackage;

    private String controllerBasePackage;

    public String getDomainBasePackage() {
        return domainBasePackage;
    }

    public void setDomainBasePackage(String domainBasePackage) {
        this.domainBasePackage = domainBasePackage;
    }

    public String getServiceBasePackage() {
        return serviceBasePackage;
    }

    public void setServiceBasePackage(String serviceBasePackage) {
        this.serviceBasePackage = serviceBasePackage;
    }

    public String getServiceImplBasePackage() {
        return serviceImplBasePackage;
    }

    public void setServiceImplBasePackage(String serviceImplBasePackage) {
        this.serviceImplBasePackage = serviceImplBasePackage;
    }

    public String getDaoBasePackage() {
        return daoBasePackage;
    }

    public void setDaoBasePackage(String daoBasePackage) {
        this.daoBasePackage = daoBasePackage;
    }

    public String getControllerBasePackage() {
        return controllerBasePackage;
    }

    public void setControllerBasePackage(String controllerBasePackage) {
        this.controllerBasePackage = controllerBasePackage;
    }
}
