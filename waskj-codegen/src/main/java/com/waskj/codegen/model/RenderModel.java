package com.waskj.codegen.model;

/**
 * Created by poet on 2016/10/27.
 */
public class RenderModel {

    // 实体类
    private String entityClassName;
    private String entityPackage;
    private String entityClassFullName;

    // dao
    private String daoClassName;
    private String daoPackage;
    private String daoClassFullName;

    // service
    private String serviceClassName;
    private String servicePackage;
    private String serviceClassFullName;

    // service impl
    private String serviceImplClassName;
    private String serviceImplPackage;
    private String serviceImplClassFullName;

    // controller
    private String controllerClassName;
    private String controllerPackage;
    private String controllerClassFullName;

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getEntityClassFullName() {
        return entityClassFullName;
    }

    public void setEntityClassFullName(String entityClassFullName) {
        this.entityClassFullName = entityClassFullName;
    }

    public String getDaoClassName() {
        return daoClassName;
    }

    public void setDaoClassName(String daoClassName) {
        this.daoClassName = daoClassName;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getDaoClassFullName() {
        return daoClassFullName;
    }

    public void setDaoClassFullName(String daoClassFullName) {
        this.daoClassFullName = daoClassFullName;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getServiceClassFullName() {
        return serviceClassFullName;
    }

    public void setServiceClassFullName(String serviceClassFullName) {
        this.serviceClassFullName = serviceClassFullName;
    }

    public String getServiceImplClassName() {
        return serviceImplClassName;
    }

    public void setServiceImplClassName(String serviceImplClassName) {
        this.serviceImplClassName = serviceImplClassName;
    }

    public String getServiceImplClassFullName() {
        return serviceImplClassFullName;
    }

    public void setServiceImplClassFullName(String serviceImplClassFullName) {
        this.serviceImplClassFullName = serviceImplClassFullName;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName = controllerClassName;
    }

    public String getServiceImplPackage() {
        return serviceImplPackage;
    }

    public void setServiceImplPackage(String serviceImplPackage) {
        this.serviceImplPackage = serviceImplPackage;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getControllerClassFullName() {
        return controllerClassFullName;
    }

    public void setControllerClassFullName(String controllerClassFullName) {
        this.controllerClassFullName = controllerClassFullName;
    }
}
