package com.waskj.base.consumer.core.support;

import com.waskj.base.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 与spring mvc的@InitBinder结合
 * 
 * 转换前台的字符串格式的日期值给后台使用
 * 
 * http://git.oschina.net/sphsyv/sypro
 * 
 * @author 孙宇
 * 
 */
public class DateTimePropertyEditorSupport extends PropertyEditorSupport {

	public DateTimePropertyEditorSupport() {
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(value) : "");
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || StringUtils.isBlank(text)) {
			setValue(null);
		} else {
			setValue(DateUtil.stringToDate(StringUtils.trim(text)));
		}
	}

}
