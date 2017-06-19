package com.waskj.base.api.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Approve {

	/**
	 * 要审批的流程号
	 * @return
	 */
	String[] value();
	
	/**
	 * 流程名字
	 * @return
	 */
	String name();
	
	/**
	 * 流程列表地址
	 * @return
	 */
	String url();
}
