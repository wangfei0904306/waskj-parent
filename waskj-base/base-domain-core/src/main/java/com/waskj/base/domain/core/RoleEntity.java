package com.waskj.base.domain.core;

import java.io.Serializable;

/**
 * 角色抽象
 */
public interface RoleEntity<ID> extends Serializable {

    String getRoleName();

    String getRoleCode();

    ID getRoleId();

}
