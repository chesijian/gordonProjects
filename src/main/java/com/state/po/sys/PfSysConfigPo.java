package com.state.po.sys;

import java.util.Date;

import com.state.po.BasePo;

public class PfSysConfigPo   extends BasePo {
	/**
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日上午11:57:55
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String key;
	private String value;
	private String type;
	private String remark;
	private String addName;
	private String addNameCn;
	private String lastModifier;
	private String lastModifierCn;
	private Date docCreated;
	private Date lastModified;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getAddNameCn() {
		return addNameCn;
	}
	public void setAddNameCn(String addNameCn) {
		this.addNameCn = addNameCn;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public String getLastModifierCn() {
		return lastModifierCn;
	}
	public void setLastModifierCn(String lastModifierCn) {
		this.lastModifierCn = lastModifierCn;
	}
	public Date getDocCreated() {
		return docCreated;
	}
	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	
}
