package com.waskj.base.api.core;

import com.baomidou.framework.service.IService;
import com.waskj.base.domain.core.BaseEntity;

import java.io.Serializable;


/**
 * 基础Service
 * 
 * http://git.oschina.net/sphsyv/sypro
 * 
 * @author 孙宇
 *
 * @param <T>
 */
public interface BaseService<T extends BaseEntity, I extends Serializable> extends IService<T, I> {

}
