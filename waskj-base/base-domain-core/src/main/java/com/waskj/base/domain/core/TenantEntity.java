package com.waskj.base.domain.core;

import java.io.Serializable;

/**
 *  租户实体类接口抽象
 */
public interface TenantEntity<ID extends Serializable> extends Serializable{

    ID getId();

    void setId(ID id);

    String getName();

    void setName(String name);


}
