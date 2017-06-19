package com.waskj.base.domain.core.support;

import com.waskj.base.domain.core.AuditableEntity;
import com.waskj.base.domain.core.BaseJpaEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@MappedSuperclass
public abstract class AbstractAuditableEntity<PK extends Serializable> extends BaseJpaEntity<PK> implements AuditableEntity {

    @Column(name = "created_date")
    protected Date createdDate;
    @Column(name = "last_modified_date")
    protected Date lastModifiedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
