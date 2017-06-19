package com.waskj.base.provider.core;

import com.waskj.base.api.core.WorkFlowCallBackService;
import com.waskj.base.api.core.WorkFlowDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by poet on 2016/9/29.
 */
@Service
public class WorkFlowDiscoveryServiceImpl implements WorkFlowDiscoveryService {

    @Autowired
    public List<WorkFlowCallBackService> callbackservicList;

    @Qualifier("defaultWfCallbackService")
    @Autowired
    public WorkFlowCallBackService defaultCallbackService;

    @Override
    public <T> WorkFlowCallBackService<T> discoveryService(Class<T> entityClass) {
        WorkFlowCallBackService<T> result = null;
        for (WorkFlowCallBackService wf : this.callbackservicList) {
            if( wf.getSupportClass() == entityClass ){
                result = wf;
                break;
            }
        }
        return result == null ? defaultCallbackService : result;
    }
}
