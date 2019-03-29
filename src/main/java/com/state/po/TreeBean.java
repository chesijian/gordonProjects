package com.state.po;

public class TreeBean {
   String id;
   String pId;
   String name;
   Boolean open;
   String icon;
   
   String area;
   String dtype;
   String drole;
   Integer sort;
   
  public Integer getSort() {
	return sort;
}

public void setSort(Integer sort) {
	this.sort = sort;
}

public String getpId() {
	return pId;
}

public void setpId(String pId) {
	this.pId = pId;
}

public String getIcon() {
	return icon;
}

public void setIcon(String icon) {
	this.icon = icon;
}

public String getArea() {
	return area;
}

public void setArea(String area) {
	this.area = area;
}

public String getDtype() {
	return dtype;
}

public void setDtype(String dtype) {
	this.dtype = dtype;
}

public String getDrole() {
	return drole;
}

public void setDrole(String drole) {
	this.drole = drole;
}

public TreeBean(String id, String pId, String name, Boolean open,String icon) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
		this.icon=icon;
	}
public TreeBean(String id, String pId, String name, Boolean open,String icon,Integer sort) {
	super();
	this.id = id;
	this.pId = pId;
	this.name = name;
	this.open = open;
	this.icon=icon;
	this.sort = sort;
}
  
public TreeBean(String id, String pId, String name, Boolean open) {
	super();
	this.id = id;
	this.pId = pId;
	this.name = name;
	this.open = open;
}
public TreeBean(String id, String pId, String name, Boolean open,Integer sort) {
	super();
	this.id = id;
	this.pId = pId;
	this.name = name;
	this.open = open;
	this.sort = sort;
}

public TreeBean(String id, String pId, String name) {
	super();
	this.id = id;
	this.pId = pId;
	this.name = name;
}


public String getId() {
	return id;
}


public void setId(String id) {
	this.id = id;
}


public String getPID() {
	return pId;
}


public void setPID(String pId) {
	this.pId = pId;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public Boolean getOpen() {
	return open;
}


public void setOpen(Boolean open) {
	this.open = open;
}
   
}
