package com.waskj.base.domain.core.support;

import com.waskj.base.domain.core.BaseJpaEntity;
import com.waskj.base.domain.core.ProcessableEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 *
 */
@MappedSuperclass
public abstract class AbstractProcessableEntity<PK extends Serializable> extends BaseJpaEntity<PK> implements ProcessableEntity {

    @Column(name = "bpm_process_id")
    protected String bpmProcessId;
    @Column(name = "process_definition_id")
    protected String processDefinitionId;
    @Column(name = "process_definition_name")
    protected String processDefinitionName;
    @Column(name = "process_key")
    protected String processKey;
    @Column(name = "current_activity", nullable = true)
    protected String currentActivity;
    @Column(name = "process_instance_id")
    protected String processInstanceId;

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getBpmProcessId() {
        return bpmProcessId;
    }

    public void setBpmProcessId(String bpmProcessId) {
        this.bpmProcessId = bpmProcessId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
