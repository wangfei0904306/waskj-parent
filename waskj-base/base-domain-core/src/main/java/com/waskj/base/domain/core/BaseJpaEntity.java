package com.waskj.base.domain.core;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */
@MappedSuperclass
public abstract class BaseJpaEntity<PK extends Serializable> extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
