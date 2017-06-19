package com.waskj.base.provider.core;

import com.waskj.base.api.core.WfDelegateCrudService;
import com.waskj.base.api.core.WorkFlowCallBackService;
import com.waskj.base.domain.core.ProcessableEntity;
import com.waskj.base.domain.core.workflow.AbstractTaskInfo;
import com.waskj.base.provider.core.util.BeanCopyUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 工作流事件默认回调
 * <br/>
 * note: 默认只在 afterProcessStarted 事件中保存实体
 * @param <T>
 */
@Service("defaultWfCallbackService")
public class DefaultWorkFlowCallBackServiceImpl<T> implements WorkFlowCallBackService<T> {

    private Logger logger = LoggerFactory.getLogger(DefaultWorkFlowCallBackServiceImpl.class);

    @Autowired
    private WfDelegateCrudService crudService;

    @Override
    public boolean beforeStartProcess(T t, Map contextMap) {
        // default pass
        logger.debug("beforeStartProcess CallBack triggered, default return true");
        return true;
    }

    @Override
    public void afterProcessStarted(T t, ProcessInstance processInstance, Map contextMap) {
        if( t instanceof ProcessableEntity)
            ((ProcessableEntity)t).setProcessInstanceId(processInstance.getId());
        crudService.save(t);
    }

    @Override
    public boolean beforeTaskComplete(T t, AbstractTaskInfo taskInfo, Map contextMap) {
        return true;
    }

    @Override
    public void afterTaskComplete(T t, AbstractTaskInfo taskInfo, Map contextMap) {
        updateProcessableEntity(t, taskInfo);
    }

    @Override
    public boolean beforeTaskRollBack(T t, AbstractTaskInfo taskInfo, Map contextMap) {
        return true;
    }

    @Override
    public void afterTaskRollBack(T t, AbstractTaskInfo taskInfo, Map contextMap) {
        updateProcessableEntity(t, taskInfo);
    }


    @Override
    public Class<T> getSupportClass() {
        // default callback service ,don't change this
        return null;
    }

    // ======= private method

    private void updateProcessableEntity(T t, AbstractTaskInfo taskInfo) {
        ProcessableEntity pe = crudService.findByTaskInfo(taskInfo,t);
        if( pe != null ){
            // copy properties from t to pe,then update this
            BeanCopyUtil.copyPropertiesIgnoreNull(t,pe);
            pe.setCurrentActivity(taskInfo.getName());
            crudService.update(pe);
        }
    }
}
