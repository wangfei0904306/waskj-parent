package com.waskj.base.domain.core.support;

import com.waskj.base.domain.core.UserEntity;

import java.io.Serializable;

/**
 *
 */
public abstract class AbstractAuditableUserEntity<ID extends Serializable> extends AbstractAuditableEntity<ID> implements UserEntity<ID> {
}
