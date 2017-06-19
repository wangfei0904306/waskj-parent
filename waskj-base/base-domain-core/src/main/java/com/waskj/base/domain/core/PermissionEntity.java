package com.waskj.base.domain.core;

import java.io.Serializable;

/**
 */
public interface PermissionEntity<ID> extends Serializable {

    String getPermissionName();

    String getPermissionCode();

    ID getPermissionId();

}
