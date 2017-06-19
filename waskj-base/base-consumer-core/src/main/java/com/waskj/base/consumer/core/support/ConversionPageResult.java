package com.waskj.base.consumer.core.support;

import com.baomidou.mybatisplus.plugins.Page;
import com.waskj.base.consumer.core.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

public abstract class ConversionPageResult<O, T> extends PageResult<T> {

	private static final long serialVersionUID = 1L;

	public abstract O coversion(T t);

	public ConversionPageResult(Page<T> page) {
		super(page);
	}

	public List getRows() {
		List<O> list = new ArrayList<O>();
		for (Object t : super.getRows()) {
			list.add(coversion((T) t));
		}
		return list;
	}

	public String[] getNullPropertyNamesFromSource (Object source,Object target) {
		List<String> targetProperties = Arrays.asList(getAllNullPropertyNamesFromTarget(target));

		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for(java.beans.PropertyDescriptor pd : pds) {
			if( targetProperties.contains(pd.getName())){

				// 过滤集合类型
				if( Collection.class.isAssignableFrom(pd.getPropertyType()) || Map.class.isAssignableFrom(pd.getPropertyType()) ){
					// 注意： 集合类型,默认全部过滤
					emptyNames.add(pd.getName());
					continue;
				}

				Object srcValue = src.getPropertyValue(pd.getName());
				if (srcValue == null)
					emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public String[] getAllNullPropertyNamesFromTarget (Object target) {
		final BeanWrapper src = new BeanWrapperImpl(target);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for(java.beans.PropertyDescriptor pd : pds) {
			emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public O copyProperties(T source,O target){
		BeanUtils.copyProperties(source,target,getNullPropertyNamesFromSource(source,target));
		return target;
	}

}
