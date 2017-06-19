package com.waskj.base.ms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 服务配置
 */
public class ServiceConfig implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

    /**
     * 应用名称
     */
    protected String applicationName;

    /**
     * IP地址，可以不配置，默认为本地ip地址
     */
    protected String host;

    /**
     * 端口
     */
    protected Integer port;

    /**
     * 服务发现客户端配置
     */
    protected Map<String,Object> discoveryClientConfig;

    /**
     * 服务暴露接口所在包
     */
    protected String exportPackage;

    /**
     * 服务引用接口所在包
     */
    protected String proxyPackage;

    /**
     * 默认构造方法，不建议使用
     */
    public ServiceConfig(){}

    public ServiceConfig(String applicationName, Integer port, Map<String,Object> discoveryClientConfig) {
        this.applicationName = applicationName;
        this.port = port;
        this.discoveryClientConfig = discoveryClientConfig;
        this.initHost();
    }

    public ServiceConfig(String applicationName, String host, Integer port, Map<String,Object> discoveryClientConfig) {
        this.applicationName = applicationName;
        this.host = host;
        this.port = port;
        this.discoveryClientConfig = discoveryClientConfig;
    }

    /**
     * 初始化Host
     */
    protected void initHost(){
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("try to get localhost address erorr, please set host by your self!",e);
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String,Object> getDiscoveryClientConfig() {
        return discoveryClientConfig;
    }

    public void setDiscoveryClientConfig(Map<String,Object> discoveryClientConfig) {
        this.discoveryClientConfig = discoveryClientConfig;
    }

    public String getExportPackage() {
        return exportPackage;
    }

    public void setExportPackage(String exportPackage) {
        this.exportPackage = exportPackage;
    }

    public String getProxyPackage() {
        return proxyPackage;
    }

    public void setProxyPackage(String proxyPackage) {
        this.proxyPackage = proxyPackage;
    }
}
