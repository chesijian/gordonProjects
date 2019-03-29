package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 山东地区费用结果表
 * 
 * @author jh
 *
 */
@Alias("SdTotalCostPo")
public class SdTotalCostPo extends BasePo {
	private static final long serialVersionUID = 1L;
	
	//成交时间
	private String dTime;
	//日期
	private String mDate;
	//地区
	private String area;
	//送端出清电量
	private Double clearQI;
	//受端出清电量
	private Double clearQJ;
	//送端上网费用
	private Double sw;
	//省内输电费用
	private Double sdsd;
	//华中输电费用
	private Double hzsd;
	//德宝输电费用
	private Double dbsd;
	//西北输电费用
	private Double xbsd;
	//宁东输电费用
	private Double ndsd;
	//从送端累加的总费用
	private Double sdfy;
	//受端总费用
	private Double fdfy;
	//跨区输电费用
	private Double kqsd;
	//地理位置
	private String position;
	public String getdTime() {
		return dTime;
	}
	public void setdTime(String dTime) {
		this.dTime = dTime;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public Double getClearQI() {
		return clearQI;
	}
	public void setClearQI(Double clearQI) {
		this.clearQI = clearQI;
	}
	public Double getClearQJ() {
		return clearQJ;
	}
	public void setClearQJ(Double clearQJ) {
		this.clearQJ = clearQJ;
	}
	public Double getSw() {
		return sw;
	}
	public void setSw(Double sw) {
		this.sw = sw;
	}
	public Double getSdsd() {
		return sdsd;
	}
	public void setSdsd(Double sdsd) {
		this.sdsd = sdsd;
	}
	public Double getHzsd() {
		return hzsd;
	}
	public void setHzsd(Double hzsd) {
		this.hzsd = hzsd;
	}
	public Double getDbsd() {
		return dbsd;
	}
	public void setDbsd(Double dbsd) {
		this.dbsd = dbsd;
	}
	public Double getXbsd() {
		return xbsd;
	}
	public void setXbsd(Double xbsd) {
		this.xbsd = xbsd;
	}
	public Double getNdsd() {
		return ndsd;
	}
	public void setNdsd(Double ndsd) {
		this.ndsd = ndsd;
	}
	public Double getSdfy() {
		return sdfy;
	}
	public void setSdfy(Double sdfy) {
		this.sdfy = sdfy;
	}
	public Double getFdfy() {
		return fdfy;
	}
	public void setFdfy(Double fdfy) {
		this.fdfy = fdfy;
	}
	public Double getKqsd() {
		return kqsd;
	}
	public void setKqsd(Double kqsd) {
		this.kqsd = kqsd;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
}
