package com.waskj.base.domain.core.workflow;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.waskj.base.domain.core.BaseEntity;

import java.util.Date;

/**
 * TaskInfo 人工任务.
 * 
 * @author Lingo
 */

@TableName("TASK_INFO")
public abstract class AbstractTaskInfo extends BaseEntity {
	@TableField(exist = false)
	private static final long serialVersionUID = 0L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**  */
	@TableField(value = "BUSINESS_KEY")
	private String businessKey;

	/**  */
	private String code;

	/**  */
	private String name;

	/**  */
	private String description;

	/**  */
	private Integer priority;

	/**  */
	private String category;

	/**  */
	private String form;

	/**  */
	@TableField(value = "TENANT_ID")
	private String tenantId;

	/**  */
	private String status;

	/**  */
	@TableField(value = "SUSPEND_STATUS")
	private String suspendStatus;

	/**  */
	@TableField(value = "DELEGATE_STATUS")
	private String delegateStatus;

	/**  */
	@TableField(value = "COMPLETE_STATUS")
	private String completeStatus;

	/**  */
	@TableField(value = "SKIP_STATUS")
	private String skipStatus;

	/**  */
	@TableField(value = "ESCALATE_STATUS")
	private String escalateStatus;

	/**  */
	@TableField(value = "COPY_STATUS")
	private String copyStatus;

	/**  */
	@TableField(value = "COPY_TASK_ID")
	private String copyTaskId;

	/**  */
	@TableField(value = "PRESENTATION_NAME")
	private String presentationName;

	/**  */
	@TableField(value = "PRESENTATION_SUBJECT")
	private String presentationSubject;

	/**  */
	@TableField(value = "CREATE_TIME")
	private Date createTime;

	/**  */
	@TableField(value = "ACTIVATION_TIME")
	private Date activationTime;

	/**  */
	@TableField(value = "CLAIM_TIME")
	private Date claimTime;

	/**  */
	@TableField(value = "COMPLETE_TIME")
	private Date completeTime;

	/**  */
	@TableField(value = "EXPIRATION_TIME")
	private Date expirationTime;

	/**  */
	@TableField(value = "LAST_MODIFIED_TIME")
	private Date lastModifiedTime;

	/**  */
	private String duration;

	/**  */
	private String creator;

	/**  */
	private String initiator;

	/**  */
	private String assignee;

	/**  */
	private String owner;

	/**  */
	@TableField(value = "LAST_MODIFIER")
	private String lastModifier;

	/**  */
	private String swimlane;

	/**  */
	@TableField(value = "PARENT_ID")
	private Long parentId;

	/**  */
	@TableField(value = "TASK_ID")
	private String taskId;

	/**  */
	@TableField(value = "EXECUTION_ID")
	private String executionId;

	/**  */
	@TableField(value = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	/**  */
	@TableField(value = "PROCESS_DEFINITION_ID")
	private String processDefinitionId;

	/**  */
	private String attr1;

	/**  */
	private String attr2;

	/**  */
	private String attr3;

	/**  */
	private String attr4;

	/**  */
	private String attr5;

	/**  */
	@TableField(value = "PROCESS_BUSINESS_KEY")
	private String processBusinessKey;

	/**  */
	@TableField(value = "PROCESS_STARTER")
	private String processStarter;

	/**  */
	private String catalog;

	/**  */
	private String action;

	/**  */
	private String comment;

	/**  */
	private String message;

	@TableField(exist = false)
	private String bpmProcessName;		// 关联的流程名称
	@TableField(exist = false)
	private String assigneeName;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getForm() {
		return this.form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuspendStatus() {
		return this.suspendStatus;
	}

	public void setSuspendStatus(String suspendStatus) {
		this.suspendStatus = suspendStatus;
	}

	public String getDelegateStatus() {
		return this.delegateStatus;
	}

	public void setDelegateStatus(String delegateStatus) {
		this.delegateStatus = delegateStatus;
	}

	public String getCompleteStatus() {
		return this.completeStatus;
	}

	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}

	public String getSkipStatus() {
		return this.skipStatus;
	}

	public void setSkipStatus(String skipStatus) {
		this.skipStatus = skipStatus;
	}

	public String getEscalateStatus() {
		return this.escalateStatus;
	}

	public void setEscalateStatus(String escalateStatus) {
		this.escalateStatus = escalateStatus;
	}

	public String getCopyStatus() {
		return this.copyStatus;
	}

	public void setCopyStatus(String copyStatus) {
		this.copyStatus = copyStatus;
	}

	public String getCopyTaskId() {
		return this.copyTaskId;
	}

	public void setCopyTaskId(String copyTaskId) {
		this.copyTaskId = copyTaskId;
	}

	public String getPresentationName() {
		return this.presentationName;
	}

	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	public String getPresentationSubject() {
		return this.presentationSubject;
	}

	public void setPresentationSubject(String presentationSubject) {
		this.presentationSubject = presentationSubject;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getActivationTime() {
		return this.activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public Date getClaimTime() {
		return this.claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public Date getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public Date getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Date getLastModifiedTime() {
		return this.lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getDuration() {
		return this.duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getInitiator() {
		return this.initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getSwimlane() {
		return this.swimlane;
	}

	public void setSwimlane(String swimlane) {
		this.swimlane = swimlane;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getAttr1() {
		return this.attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return this.attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return this.attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	public String getAttr4() {
		return this.attr4;
	}

	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}

	public String getAttr5() {
		return this.attr5;
	}

	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}

	public String getProcessBusinessKey() {
		return this.processBusinessKey;
	}

	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}

	public String getProcessStarter() {
		return this.processStarter;
	}

	public void setProcessStarter(String processStarter) {
		this.processStarter = processStarter;
	}

	public String getCatalog() {
		return this.catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBpmProcessName() {
		return bpmProcessName;
	}

	public void setBpmProcessName(String bpmProcessName) {
		this.bpmProcessName = bpmProcessName;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
}
