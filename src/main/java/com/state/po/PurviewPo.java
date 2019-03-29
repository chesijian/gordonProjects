package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 权限控制表
 */
@SuppressWarnings("serial")
@Alias("PurviewPo")
public class PurviewPo extends BasePo {
	/**
	 * 功能名称
	 */
	private String gname;

	/**
	 * 功能状态
	 */
	private String dtype;

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

}
