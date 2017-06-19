package com.waskj.base.domain.core;

import java.util.Date;

/**
 * 可审计实体，抽象 createTime updateTime
 */
public interface AuditableEntity {

    // 创建时间
    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastModifiedDate();

    void setLastModifiedDate(Date lastModifiedDate);

//    // 创建人
//    Long getCreateBy();
//
//    void setCreateBy(Long pk);
//
//    // 更新人
//    Long getUpdateBy();
//
//    void setUpdateBy(Long updateBy);

}
