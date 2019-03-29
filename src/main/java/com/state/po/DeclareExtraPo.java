package com.state.po;

public class DeclareExtraPo extends BasePo{

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:10:09
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String sheetUid;
	String startTime;
	String endTime;
	float power;
	float price;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSheetUid() {
		return sheetUid;
	}
	public void setSheetUid(String sheetUid) {
		this.sheetUid = sheetUid;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public float getPower() {
		return power;
	}
	public void setPower(float power) {
		this.power = power;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	

}
