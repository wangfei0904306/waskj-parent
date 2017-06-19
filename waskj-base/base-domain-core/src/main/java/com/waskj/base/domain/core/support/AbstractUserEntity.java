package com.waskj.base.domain.core.support;

import com.waskj.base.domain.core.BaseJpaEntity;
import com.waskj.base.domain.core.UserEntity;

import java.io.Serializable;

/**
 *
 */
public abstract class AbstractUserEntity<ID extends Serializable> extends BaseJpaEntity<ID> implements UserEntity<ID> {
}
