package com.state.util;

public class MChildrenTree {
	private String id;
    private String pId;
    private String iconCls;
    private Attributes attributes;
    private boolean isBill;

	public boolean isBill() {
		return isBill;
	}

	public void setBill(boolean isBill) {
		this.isBill = isBill;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private String text;
	private String state;
}
