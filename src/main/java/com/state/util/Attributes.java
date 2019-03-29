package com.state.util;

public class Attributes {
	private String id;
	private String type;
	private Double sumQ;
	private Double sumPrice;
	// 买卖类型
		private String drloe;
	//开始时间
	private String startDate;
	//结束时间
	private String endDate;
	//签字
	private String rname;
	// 备注
	private String descr;
	
	private String mdate;
	
	private String area;
	
	
	private String clearstate;
	
	private int status;
	
	/**
	 * 是否允许编辑
	 */
	private boolean isAllowEdit;
	
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public boolean isAllowEdit() {
		return isAllowEdit;
	}

	public void setAllowEdit(boolean isAllowEdit) {
		this.isAllowEdit = isAllowEdit;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getSumQ() {
		return sumQ;
	}

	public void setSumQ(Double sumQ) {
		this.sumQ = sumQ;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getClearstate() {
		return clearstate;
	}

	public void setClearstate(String clearstate) {
		this.clearstate = clearstate;
	}

	public String getDrloe() {
		return drloe;
	}

	public void setDrloe(String drloe) {
		this.drloe = drloe;
	}
	
	
    
}
