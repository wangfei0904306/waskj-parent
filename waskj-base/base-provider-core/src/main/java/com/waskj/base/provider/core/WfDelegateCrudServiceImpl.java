package com.waskj.base.provider.core;

import com.waskj.base.api.core.WfDelegateCrudService;
import com.waskj.base.domain.core.AuditableEntity;
import com.waskj.base.domain.core.ProcessableEntity;
import com.waskj.base.domain.core.workflow.AbstractTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by poet on 2016/9/29.
 */
@Service
public class WfDelegateCrudServiceImpl implements WfDelegateCrudService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Serializable save(Object entity) {
        if( entity instanceof AuditableEntity){
            AuditableEntity ae = (AuditableEntity)entity;
            ae.setCreatedDate(new Date());
        }
        return hibernateTemplate.save(entity);
    }

    @Override
    public ProcessableEntity findByTaskInfo(AbstractTaskInfo taskInfo, Object entity) {
        String processInstanceId = taskInfo.getProcessInstanceId();
        String processDefinitionId = taskInfo.getProcessDefinitionId();
        if( entity == null )
            return null;
        if(!(entity instanceof ProcessableEntity))
            return null;

        Class clazz = entity.getClass();

        String hql = "FROM " + clazz.getName() + " WHERE " + " processInstanceId = ? and processDefinitionId = ?";

        List list = hibernateTemplate.find(hql,processInstanceId,processDefinitionId);

        return list.size() > 0 ? (ProcessableEntity) list.get(0) : null;
    }
    // TODO 设置 updateTime，CreateTime createBy
    public void update(Object entity){
        if( entity instanceof AuditableEntity ){
            AuditableEntity ae = (AuditableEntity)entity;
            ae.setLastModifiedDate(new Date());
        }
        hibernateTemplate.update(entity);
    }
}
