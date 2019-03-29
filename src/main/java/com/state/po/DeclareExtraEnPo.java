package com.state.po;

public class DeclareExtraEnPo extends BasePo{

	/**
	 * 加密的数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:10:09
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String sheetUid;
	String startTime;
	String endTime;
	String power;
	String price;
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
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	

}
