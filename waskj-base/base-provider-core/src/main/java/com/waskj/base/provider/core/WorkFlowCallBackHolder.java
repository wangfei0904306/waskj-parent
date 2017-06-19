package com.waskj.base.provider.core;

import com.waskj.base.api.core.WorkFlowCallBackService;
import com.waskj.base.api.core.WorkFlowDiscoveryService;
import com.waskj.base.core.util.spring.SpringContextHolder;
import com.waskj.base.domain.core.workflow.AbstractTaskInfo;
import com.waskj.base.provider.core.util.Map2BeanConverter;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
public class WorkFlowCallBackHolder {

    private static Logger logger = LoggerFactory.getLogger(WorkFlowCallBackHolder.class);

    // constants
    private static final String ENTITY_KEY = "_ENTITY_";

    private static ThreadLocal<Map> requestParamHolder = new ThreadLocal<Map>();
    private static ThreadLocal<WorkFlowCallBackService> callBackServiceHolder = new ThreadLocal<>();
    private static ThreadLocal<Object> entityHolder = new ThreadLocal<>();
    private static ThreadLocal<Class> entityClassHolder = new ThreadLocal<>();

    public static void pushRequestParams(Map requestParams){
        requestParamHolder.set(requestParams);

        Class clazz = null;
        try {
            clazz = Class.forName(getEntityName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        WorkFlowCallBackService callBackServie = SpringContextHolder.getBean(WorkFlowDiscoveryService.class).discoveryService(getEntityClass());
        entityClassHolder.set(clazz);
        Object entity = Map2BeanConverter.convert(getRequestMap(),clazz);
        callBackServiceHolder.set(callBackServie);
        entityHolder.set(entity);

    }

    public static Map getRequestMap(){
        return requestParamHolder.get();
    }

    public static String getEntityName(){
        return String.valueOf(getRequestMap().get(ENTITY_KEY));
    }

    public static Class getEntityClass(){
        return entityClassHolder.get();
    }

    public static <T> T getEntityFromReqMap(){
        return (T)entityHolder.get();
    }

    public static <T> WorkFlowCallBackService<T> getCallBackService(){
        return callBackServiceHolder.get();
    }


    // call back methods adapter
    public static boolean beforeProcessStart(){
        return getCallBackService().beforeStartProcess(getEntityFromReqMap(),getRequestMap());
    }

    public static boolean beforeTaskComplete(AbstractTaskInfo taskInfo){
        return getCallBackService().beforeTaskComplete(getEntityFromReqMap(),taskInfo,getRequestMap());
    }

    public static boolean beforeTaskRollBack(AbstractTaskInfo taskInfo){
        return getCallBackService().beforeTaskRollBack(getEntityFromReqMap(),taskInfo,getRequestMap());
    }

    public static void afterProcessStarted(ProcessInstance processInstance){
        getCallBackService().afterProcessStarted(getEntityFromReqMap(),processInstance,getRequestMap());
    }

    public static void afterTaskCompleted(AbstractTaskInfo taskInfo){
        getCallBackService().afterTaskComplete(getEntityFromReqMap(),taskInfo,getRequestMap());
    }

    public static void afterTaskRollBacked(AbstractTaskInfo taskInfo){
        getCallBackService().afterTaskRollBack(getEntityFromReqMap(),taskInfo,getRequestMap());
    }

}
