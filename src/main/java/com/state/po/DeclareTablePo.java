package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 申报数据表格
 * @author jh
 *
 */
@Alias("DeclareTablePo")
public class DeclareTablePo extends BasePo {
	private static final long serialVersionUID = 1L;
	// 地区
	private String area;
	// 平均电量
	private double powerAvg;
	//平均电价
	private double priceAvg;
	
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public double getPowerAvg() {
		return powerAvg;
	}
	public void setPowerAvg(double powerAvg) {
		this.powerAvg = powerAvg;
	}
	public double getPriceAvg() {
		return priceAvg;
	}
	public void setPriceAvg(double priceAvg) {
		this.priceAvg = priceAvg;
	}
	
	
}
