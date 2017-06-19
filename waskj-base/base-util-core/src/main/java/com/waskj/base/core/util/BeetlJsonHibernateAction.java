package com.waskj.base.core.util;

import org.beetl.json.ActionReturn;
import org.beetl.json.JsonWriter;
import org.beetl.json.OutputNode;
import org.beetl.json.OutputNodeKey;
import org.beetl.json.action.IValueAction;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * 为了不让hibernate对象序列化出错，添加一个匹配动作，所有属性都会执行hinernateCheck的回调
 * 
 * @author 孙宇
 *
 */
public class BeetlJsonHibernateAction implements IValueAction {

	@Override
	public ActionReturn doit(OutputNodeKey field, Object value, OutputNode thisNode, JsonWriter w) {
		if (value instanceof HibernateProxy) {// hibernate代理对象
			LazyInitializer initializer = ((HibernateProxy) value).getHibernateLazyInitializer();
			if (initializer.isUninitialized()) {
				return new ActionReturn(null, ActionReturn.RETURN);
			}
		} else if (value instanceof PersistentCollection) {// 实体关联集合
			PersistentCollection collection = (PersistentCollection) value;
			if (!collection.wasInitialized()) {
				return new ActionReturn(null, ActionReturn.RETURN);
			} else if (collection.getValue() == null) {
				return new ActionReturn(null, ActionReturn.RETURN);
			}
		}
		return new ActionReturn(value, ActionReturn.CONTINUE);
	}

}
