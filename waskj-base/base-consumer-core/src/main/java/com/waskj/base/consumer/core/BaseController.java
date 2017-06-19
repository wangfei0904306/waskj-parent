package com.waskj.base.consumer.core;

import com.baomidou.framework.controller.SuperController;
import com.baomidou.mybatisplus.plugins.Page;
import com.waskj.base.api.core.BaseService;
import com.waskj.base.consumer.core.format.MultiDateFormat;
import com.waskj.base.consumer.core.result.JsonResult;
import com.waskj.base.consumer.core.result.PageResult;
import com.waskj.base.domain.core.BaseEntity;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础控制器
 * 
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 * 
 * 此类还有一个序列化json的方法，可以返回你需要的属性
 * 
 * http://git.oschina.net/sphsyv/sypro
 * 
 * @author 孙宇
 *
 */
public abstract class BaseController<T extends BaseEntity, I extends Serializable> extends SuperController {

	protected static final String MSG_SUCCESS = "操作成功";
	protected static final String MSG_ERROR = "操作失败";

	public abstract BaseService<T, I> getService();

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
//		binder.registerCustomEditor(Date.class,
//				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(Date.class,
				new CustomDateEditor(new MultiDateFormat(), true));

		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
	}

	@Override
	protected String getPageIndexParamName() {
		return "_index";
	}

	@Override
	protected String getPageSizeParamName() {
		return "_size";
	}

	protected JsonResult save(T t) {
		JsonResult result = new JsonResult();
		boolean b = getService().insert(t);
		message(result, b);
		return result;
	}

	protected JsonResult del(I i) {
		JsonResult result = new JsonResult();
		boolean b = getService().deleteById(i);
		message(result, b);
		return result;
	}

	protected JsonResult edit(T t) {
		JsonResult result = new JsonResult();
		boolean b = getService().updateById(t);
		message(result, b);
		result.setSuccess(b);
		return result;
	}

	protected JsonResult editSelective(T t) {
		JsonResult result = new JsonResult();
		boolean b = getService().updateSelectiveById(t);
		message(result, b);
		result.setSuccess(b);
		return result;
	}

	protected JsonResult show(I i) {
		JsonResult result = new JsonResult();
		T t = getService().selectById(i);
		result.setSuccess(true);
		result.setObj(t);
		return result;
	}

	protected PageResult list() {

		return list(null);
	}

	protected PageResult list(T entity) {
		Page<T> page = getService().selectPage(getPage(), entity);
		return new PageResult(page);
	}

	protected void message(JsonResult result, boolean b) {
		if (b) {
			result.setMsg(MSG_SUCCESS);
		} else {
			result.setMsg(MSG_ERROR);
		}
		result.setSuccess(b);
	}
	protected JsonResult message( boolean b) {
		JsonResult result = new JsonResult();
		if (b) {
			result.setMsg(MSG_SUCCESS);
		} else {
			result.setMsg(MSG_ERROR);
		}
		result.setSuccess(b);
		return result;
	}
	protected JsonResult message( boolean b,Object obj) {
		JsonResult result = new JsonResult();
		if (b) {
			result.setMsg(MSG_SUCCESS);
		} else {
			result.setMsg(MSG_ERROR);
		}
		result.setSuccess(b);
		result.setObj(obj);
		return result;
	}

	class StringEscapeEditor extends PropertyEditorSupport {

		private boolean escapeHTML;// 编码HTML
		private boolean escapeJavaScript;// 编码javascript

		public StringEscapeEditor() {
		}

		public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript) {
			this.escapeHTML = escapeHTML;
			this.escapeJavaScript = escapeJavaScript;
		}

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : "";
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			} else {
				String value = text;
				if (escapeHTML) {
					value = HtmlUtils.htmlEscape(value);
				}
				if (escapeJavaScript) {
					value = JavaScriptUtils.javaScriptEscape(value);
				}
				setValue(value);
			}
		}

	}

}