package com.state.util;

public class AreaCountBean {
	  private String area;//地区
	  private Double sumQ;//申报电量
	  private String dtype;//申报类型
 	  private Double clear;//出清电量
 	  private Double fdfy;//出清费用
 	  private Double sumPrice;//总费用
 	  
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public Double getClear() {
		return clear;
	}
	public void setClear(Double clear) {
		this.clear = clear;
	}
	
	public Double getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}
	public Double getFdfy() {
		return fdfy;
	}
	public void setFdfy(Double fdfy) {
		this.fdfy = fdfy;
	}

	public Double getSumQ() {
		return sumQ;
	}
	public void setSumQ(Double sumQ) {
		this.sumQ = sumQ;
	}
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	
}
