package com.state.po.org;

import com.state.po.BasePo;

/**
 * 权限
 * @author chesj
 *
 */
public class OrgResourcesPo extends BasePo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String pid;
	private String enabled;
	private String resourceId;
	private String remark;
	private String name;
	private String type;

	public OrgResourcesPo() {
	}
	
	public OrgResourcesPo(String id, String pid, String enabled,
			String resourceId, String remark, String name, String type) {
		this.id = id;
		this.pid = pid;
		this.enabled = enabled;
		this.resourceId = resourceId;
		this.remark = remark;
		this.name = name;
		this.type = type;
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

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
