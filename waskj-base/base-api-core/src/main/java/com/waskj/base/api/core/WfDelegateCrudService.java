package com.waskj.base.api.core;


import com.waskj.base.domain.core.ProcessableEntity;
import com.waskj.base.domain.core.workflow.AbstractTaskInfo;

import java.io.Serializable;

/**
 * CRUD接口，无泛型，工作流回调使用
 */
public interface WfDelegateCrudService {

    Serializable save(Object entity);

    // TODO 抽象TaskInfo
    ProcessableEntity findByTaskInfo(AbstractTaskInfo taskInfo, Object entity);

    void update(Object entity);

}
