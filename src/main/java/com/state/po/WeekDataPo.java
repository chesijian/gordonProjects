package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 周报数据表
 * @author 车斯剑
 *
 */
@Alias("WeekDataPo")
public class WeekDataPo extends BasePo {
	private static final long serialVersionUID = 1L;
	private String id;
	// 年份
	private String year;
	// 周数
	private int weekNum;
	private String startTime; //开始时间
	private String endTime; //结束时间
	// 天数
	private int days;
	// 可用容量
	private double ablePower;
	private double alitAvg; //通道平均利用率
	// 买方申报电量
	private double buySumElectricity;
	//卖方申报电量
	private double saleSumElectricity;
	private int buySum;   //买方申报单数据
	private int saleSum;   //卖方申报单数据
	//卖方平均申报电量
	private double saleAvgPower;
	//卖方平均申报电价
	private double saleAvgPrice;
	//买方平均申报电量
	private double buyAvgPower;
	//买方平均申报电价
	private double buyAvgPrice;
	//成交电量
	private double dealPower;
	private double dealPrice; //出清电价
	//执行电量
	private double executePower;
	
	private String pictureData; //图片数据
	//卖单申报表格
	private String saleTable;
	//买单申报表格
	private String buyTable;
	//成交结果表
	private String dealTable;
	//执行结果表
	private String executeTable;
	//偏差情况表
	private String biasTable;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPictureData() {
		return pictureData;
	}
	public void setPictureData(String pictureData) {
		this.pictureData = pictureData;
	}
	public double getAlitAvg() {
		return alitAvg;
	}
	public void setAlitAvg(double alitAvg) {
		this.alitAvg = alitAvg;
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
	public double getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(double dealPrice) {
		this.dealPrice = dealPrice;
	}
	public double getSaleAvgPower() {
		return saleAvgPower;
	}
	public void setSaleAvgPower(double saleAvgPower) {
		this.saleAvgPower = saleAvgPower;
	}
	public double getSaleAvgPrice() {
		return saleAvgPrice;
	}
	public void setSaleAvgPrice(double saleAvgPrice) {
		this.saleAvgPrice = saleAvgPrice;
	}
	public double getBuyAvgPower() {
		return buyAvgPower;
	}
	public void setBuyAvgPower(double buyAvgPower) {
		this.buyAvgPower = buyAvgPower;
	}
	public double getBuyAvgPrice() {
		return buyAvgPrice;
	}
	public void setBuyAvgPrice(double buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}
	public double getBuySumElectricity() {
		return buySumElectricity;
	}
	public void setBuySumElectricity(double buySumElectricity) {
		this.buySumElectricity = buySumElectricity;
	}
	public double getSaleSumElectricity() {
		return saleSumElectricity;
	}
	public void setSaleSumElectricity(double saleSumElectricity) {
		this.saleSumElectricity = saleSumElectricity;
	}
	public int getBuySum() {
		return buySum;
	}
	public void setBuySum(int buySum) {
		this.buySum = buySum;
	}
	public int getSaleSum() {
		return saleSum;
	}
	public void setSaleSum(int saleSum) {
		this.saleSum = saleSum;
	}
	public String getSaleTable() {
		return saleTable;
	}
	public void setSaleTable(String saleTable) {
		this.saleTable = saleTable;
	}
	public String getBuyTable() {
		return buyTable;
	}
	public void setBuyTable(String buyTable) {
		this.buyTable = buyTable;
	}
	public String getDealTable() {
		return dealTable;
	}
	public void setDealTable(String dealTable) {
		this.dealTable = dealTable;
	}
	public String getExecuteTable() {
		return executeTable;
	}
	public void setExecuteTable(String executeTable) {
		this.executeTable = executeTable;
	}
	public String getBiasTable() {
		return biasTable;
	}
	public void setBiasTable(String biasTable) {
		this.biasTable = biasTable;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public int getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(int weekNum) {
		this.weekNum = weekNum;
	}
	
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public double getAblePower() {
		return ablePower;
	}
	public void setAblePower(double ablePower) {
		this.ablePower = ablePower;
	}
	public double getDealPower() {
		return dealPower;
	}
	public void setDealPower(double dealPower) {
		this.dealPower = dealPower;
	}
	public double getExecutePower() {
		return executePower;
	}
	public void setExecutePower(double executePower) {
		this.executePower = executePower;
	}

}
