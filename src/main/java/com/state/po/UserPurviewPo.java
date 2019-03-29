package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 用户权限控制表
 */
@SuppressWarnings("serial")
@Alias("UserPurviewPo")
public class UserPurviewPo extends BasePo {
	/**
	 * 用户名
	 */
	private String mname;

	/**
	 * 功能名称
	 */
	private String gname;

	/**
	 * 功能状态
	 */
	private String dtype;

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

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
