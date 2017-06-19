package com.waskj.base.ms.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by poet on 2016/7/13.
 */
class EurekaHolderThread extends Thread {

    private final Logger logger = LoggerFactory.getLogger(EurekaHolderThread.class);

    private DynamicPropertyFactory configInstance;
    private ApplicationInfoManager applicationInfoManager;
    private EurekaClient eurekaClient;
    private String vipAddress;

    private InstanceInfo.InstanceStatus instanceStatus = InstanceInfo.InstanceStatus.UP;
    private AtomicBoolean isShutdown = new AtomicBoolean(false);

    public EurekaHolderThread(ApplicationInfoManager applicationInfoManager,EurekaClient eurekaClient,DynamicPropertyFactory configInstance){
        this.applicationInfoManager = applicationInfoManager;
        this.eurekaClient = eurekaClient;
        this.configInstance = configInstance;
    }

    public EurekaHolderThread(ApplicationInfoManager applicationInfoManager,EurekaClient eurekaClient,DynamicPropertyFactory configInstance,String vipAddress){
        this(applicationInfoManager,eurekaClient,configInstance);
        this.vipAddress = vipAddress;
    }

    public void run() {
        logger.info("the eureka holder thread is running");
        applicationInfoManager.setInstanceStatus(this.getStatus());

        logger.info("eureka instance registed success !");
        while( !this.isInterrupted() && !isShutdown.get()  ){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("set instance status error",e);
            }
        }

    }

    synchronized void setStatus(InstanceInfo.InstanceStatus status){
        this.instanceStatus = instanceStatus;
    }

    synchronized InstanceInfo.InstanceStatus getStatus(){
        return this.instanceStatus;
    }

    void setShutdown(boolean shutdown) {
        isShutdown.set(shutdown);
    }
}
