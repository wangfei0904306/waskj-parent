package com.waskj.base.consumer.core.fastjson;

import com.alibaba.fastjson.serializer.PropertyFilter;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class HibernatePropertyFilter implements PropertyFilter {

	@Override
	public boolean apply(Object object, String propertyName, Object property) {
		// TODO Auto-generated method stub
		if (property instanceof HibernateProxy) {// hibernate代理对象
			LazyInitializer initializer = ((HibernateProxy) property).getHibernateLazyInitializer();
			if (initializer.isUninitialized()) {
				return false;
			}
		} else if (property instanceof PersistentCollection) {// 实体关联一对多
			PersistentCollection collection = (PersistentCollection) property;
			if (!collection.wasInitialized()) {
				return false;
			}
		}
		return true;
	}

}
