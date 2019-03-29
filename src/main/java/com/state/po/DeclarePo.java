package com.state.po;

import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * 申报单子
 * 
 * @author 帅
 *
 */
@SuppressWarnings("serial")
@Alias("DeclarePo")
public class DeclarePo extends BasePo {
	// 申报单ID
	private long id;
	// 申报单名
	private String sheetName;
	// 备注
	private String descr;
	// 日期
	private String mdate;
	// 地区
	private String area;
	// 申报时间
	private String dtime;
	// 用户名
	private String mname;
	// 买卖类型
	private String drloe;
	
	//开始时间
	private String startDate;
	//结束时间
	private String endDate;
	//签字
	private String rname;
	//出清审核状态
	private String clearstate;
	/**
	 * 状态，默认是0可以编辑，如果提交之后变为1，则无法再次编辑
	 */
	private int status;
	
	/**
	 * 一个单一天的总电力
	 */
	private double sumPower;
	/**
	 * 总电量
	 */
	private double sumElectricity;
	/**
	 * 总电价
	 */
	private double sumPrice;
	private double avgPower;
	private double avgPrice;
	private double avgElectricity;
	
	
	
	public double getSumElectricity() {
		return sumElectricity;
	}

	public void setSumElectricity(double sumElectricity) {
		this.sumElectricity = sumElectricity;
	}

	public double getAvgElectricity() {
		return avgElectricity;
	}

	public void setAvgElectricity(double avgElectricity) {
		this.avgElectricity = avgElectricity;
	}

	public double getSumPower() {
		return sumPower;
	}

	public void setSumPower(double sumPower) {
		this.sumPower = sumPower;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public double getAvgPower() {
		return avgPower;
	}

	public void setAvgPower(double avgPower) {
		this.avgPower = avgPower;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	// 申报数据
	private List<DeclareDataPo> declareDatas;

	/**
	 * 报价区间
	 */
	private List<DeclareExtraPo> declareExtras;
	private List<DeclareExtraEnPo> declareExtraEns;

	
	public List<DeclareExtraEnPo> getDeclareExtraEns() {
		return declareExtraEns;
	}

	public void setDeclareExtraEns(List<DeclareExtraEnPo> declareExtraEns) {
		this.declareExtraEns = declareExtraEns;
	}

	public List<DeclareExtraPo> getDeclareExtras() {
		return declareExtras;
	}

	public void setDeclareExtras(List<DeclareExtraPo> declareExtras) {
		this.declareExtras = declareExtras;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDtime() {
		return dtime;
	}

	public void setDtime(String dtime) {
		this.dtime = dtime;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	
	public String getDrloe() {
		return drloe;
	}

	public void setDrloe(String drloe) {
		this.drloe = drloe;
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

	public List<DeclareDataPo> getDeclareDatas() {
		return declareDatas;
	}

	public void setDeclareDatas(List<DeclareDataPo> declareDatas) {
		this.declareDatas = declareDatas;
	}

	public String getClearstate() {
		return clearstate;
	}

	public void setClearstate(String clearstate) {
		this.clearstate = clearstate;
	}

}
