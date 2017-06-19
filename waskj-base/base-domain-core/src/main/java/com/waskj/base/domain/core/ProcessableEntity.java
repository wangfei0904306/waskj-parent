package com.waskj.base.domain.core;

/**
 * 流程实体
 */
public interface ProcessableEntity {

    void setBpmProcessId(String bpmProcessId);

    String getBpmProcessId();

    void setProcessKey(String processKey);

    String getProcessKey();

    void setProcessDefinitionName(String processDefinitionName);

    String getProcessDefinitionName();

    void setProcessDefinitionId(String processDefinitionId);

    String getProcessDefinitionId();

    void setCurrentActivity(String currentActivity);

    String getCurrentActivity();

    void setProcessInstanceId(String processInstanceId);

    String getProcessInstanceId();
}
