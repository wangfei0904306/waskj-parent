package com.waskj.base.domain.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * zTree使用的模型
 * 
 */
public class TreeVO implements Serializable {

	private String id;
	private String pid;
	private String name;
	private Boolean open;
	private Boolean checked;
	private List children = new ArrayList();

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
