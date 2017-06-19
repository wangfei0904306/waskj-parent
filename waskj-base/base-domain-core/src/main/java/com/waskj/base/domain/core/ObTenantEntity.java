package com.waskj.base.domain.core;

import java.io.Serializable;

/**
 *  拥有租户实体类接口抽象
 */
public interface ObTenantEntity<ID extends Serializable> extends Serializable{

    TenantEntity getTenantEntity();

    void setTenantEntity(TenantEntity t);


}
