package com.waskj.base.ms.dto;

/**
 *
 */
public class DiscoveryResult {

    private String host;
    private Integer port;
    private boolean founded = false;

    public DiscoveryResult(String host,Integer port,boolean founded){
        this.host = host;
        this.port = port;
        this.founded = founded;
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

    public boolean getFounded(){
        return this.founded;
    }
}
