package com.waskj.base.ms.eureka;

import com.waskj.base.ms.api.ServiceDiscoveryService;
import com.waskj.base.ms.api.ServiceRegistrationService;
import com.waskj.base.ms.api.ServiceStatusService;
import com.waskj.base.ms.config.ServiceConfig;
import com.waskj.base.ms.dto.DiscoveryResult;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务客户端
 * @author 徐成龙
 */
public class EurekaServiceClient implements ServiceDiscoveryService,ServiceRegistrationService,ServiceStatusService {

    private final Logger logger = LoggerFactory.getLogger(EurekaServiceClient.class);

    protected EurekaClient eurekaClient;

    protected ApplicationInfoManager applicationInfoManager;

    protected DynamicPropertyFactory configInstance;

    protected InstanceInfo instanceInfo;

    private EurekaHolderThread eurekaHolderThread;

    // 是否开始 EurekaHolder 线程
    private boolean useEurekaHolderThread = true;

    @Override
    public void doRegistration(ServiceConfig config) {
        this.initEurekaClient(config);
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.STARTING);
        determineEurekaHolderThread();
    }

    @Override
    public DiscoveryResult discoveryService(String serviceName) {
        DiscoveryResult result = null;

        if( this.eurekaClient == null ){
            logger.error("can not discovery service, the eureka client is not initialized!");
            result = new DiscoveryResult(null,null,false);
            return result;
        }

        try {
            InstanceInfo info = this.eurekaClient.getNextServerFromEureka(serviceName,false);
            result = new DiscoveryResult(info.getHostName(),info.getPort(),true);
        } catch (Exception e) {
            logger.error("can not discovery service with service name " + serviceName,e);
            result = new DiscoveryResult(null,null,false);
        }
        return result;
    }

    // region implement ServiceStatusService Interface
    @Override
    public void statusStarting() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.STARTING);
    }

    @Override
    public void statusUp() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    @Override
    public void statusDown() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
    }

    @Override
    public void statusUnknow() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UNKNOWN);
    }

    @Override
    public void statusOutOfService() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.OUT_OF_SERVICE);
    }
    // endregion

    protected void initEurekaClient(ServiceConfig config) {
        // init config
        if (configInstance == null) {
            configInstance = DynamicPropertyFactory
                    .initWithConfigurationSource(new ConcurrentMapConfiguration(config.getDiscoveryClientConfig()));
        }
        // init ApplicationInfoManager
        if (applicationInfoManager == null) {
            EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig();
            instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
            applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
        }
        // init EurekaClient
        if (eurekaClient == null) {
            eurekaClient = new DiscoveryClient(applicationInfoManager,new DefaultEurekaClientConfig());
        }
    }

    protected void determineEurekaHolderThread(){
        if( this.useEurekaHolderThread ){
            this.eurekaHolderThread = new EurekaHolderThread(applicationInfoManager,eurekaClient,configInstance);
            eurekaHolderThread.setPriority(3);
            this.eurekaHolderThread.start();
        }
    }

    /**
     * destory the client
     */
    public void destory(){
        // shutdown the holder thread
        if( this.useEurekaHolderThread )
            eurekaHolderThread.setShutdown(true);
        // shutdown eureka client
        this.eurekaClient.shutdown();
    }

    public void setUseEurekaHolderThread(boolean useEurekaHolderThread) {
        this.useEurekaHolderThread = useEurekaHolderThread;
    }
}
