package com.waskj.base.domain.core.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 流程权限审批VO
 * 
 * @author G.Sun
 *
 */
public class ApprovePermissionVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String setpCode;
	private String name;
	private String url;
	private String[] permissions;
	private Integer count;

	public ApprovePermissionVO(String setpCode, String name, String url, String[] permissions) {
		super();
		this.setpCode = setpCode;
		this.name = name;
		this.url = url;
		this.permissions = permissions;
	}
	@JSONField(serialize = false)
	public String getSetpCode() {
		return setpCode;
	}

	public void setSetpCode(String setpCode) {
		this.setpCode = setpCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JSONField(serialize = false)
	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	

}
