package com.waskj.base.provider.core;

import com.baomidou.framework.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.waskj.base.api.core.BaseService;
import com.waskj.base.domain.core.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;


/**
 * 基础service实现类
 * 
 * 实现此service的类必须提供泛型dao
 * 
 * http://git.oschina.net/sphsyv/sypro
 * 
 * @author 孙宇
 *
 * @param <T>
 */
@Service("baseService")
public class BaseServiceImpl<M extends BaseMapper<T, I>, T extends BaseEntity, I extends Serializable>
		extends ServiceImpl<BaseMapper<T, I>, T, I> implements BaseService<T, I> {
	@Autowired
	protected M baseMapper;


}
