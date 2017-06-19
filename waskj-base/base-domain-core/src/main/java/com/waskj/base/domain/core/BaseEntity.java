package com.waskj.base.domain.core;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;

public class BaseEntity implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

}
