package com.waskj.base.api.core;

import com.waskj.base.domain.core.workflow.AbstractTaskInfo;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 通用工作流回调接口
 */
public interface WorkFlowCallBackService<T> {

    /**
     * 流程发起之前回调
     * @param t
     * @param contextMap
     * @return
     */
    boolean beforeStartProcess(T t, Map contextMap);

    /**
     * 流程发起之后回调
     * @param t
     * @param processInstance
     * @param contextMap
     * @return
     */
    void afterProcessStarted(T t, ProcessInstance processInstance, Map contextMap);

    /**
     * 任务节点通过之前触发
     * @param t
     * @param taskInfo
     * @param contextMap
     * @return
     */
    boolean beforeTaskComplete(T t, AbstractTaskInfo taskInfo, Map contextMap);

    /**
     * 任务节点通过之后触发
     * @param t
     * @param taskInfo
     * @param contextMap
     */
    void afterTaskComplete(T t, AbstractTaskInfo taskInfo, Map contextMap);

    /**
     * 任务节点回退之前触发
     * @param t
     * @param taskInfo
     * @param contextMap
     * @return
     */
    boolean beforeTaskRollBack(T t, AbstractTaskInfo taskInfo, Map contextMap);

    /**
     * 任务节点回退之后触发
     * @param t
     * @param taskInfo
     * @param contextMap
     */
    void afterTaskRollBack(T t, AbstractTaskInfo taskInfo, Map contextMap);

    /**
     * 获取支持的实体类型
     * @return
     */
    Class<T> getSupportClass();
}
