package com.waskj.base.ms.eureka;

import com.waskj.base.ms.config.ServiceConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by poet on 2017/2/21.
 */

public class RegistrationTest {

    public static void main(String[] args) {

        EurekaServiceClient client = new EurekaServiceClient();

        ServiceConfig config = new ServiceConfig();
        config.setPort(8080);
        config.setApplicationName("DEMO-APP");
        Map<String,Object> map = new HashMap<>();
        config.setDiscoveryClientConfig(map);

        map.put("eureka.region","default");
        map.put("eureka.name",config.getApplicationName());
        map.put("eureka.vipAddress",config.getApplicationName());
        map.put("eureka.port",8080);
        map.put("eureka.fetchRegistry",false);
        map.put("eureka.instanceId","${eureka.vipAddress}:${eureka.port}");
        map.put("eureka.serviceUrl.default","http://192.168.1.70:8761/eureka/v2");

        client.doRegistration(config);

        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(".......................");
                client.destory();
                timer.cancel();
            }
        },5000);
    }


}
