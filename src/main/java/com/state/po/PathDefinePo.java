package com.state.po;

import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * 通道定义表
 */
@SuppressWarnings("serial")
@Alias("PathDefinePo")
public class PathDefinePo extends BasePo {
	/**
	 * 通道Id
	 */
	private String id;
	
	/**
	 * 通道名
	 */
	private String mpath;

	/**
	 * 优先使用顺序
	 */
	private Integer morder;

	/**
	 * 首端
	 */
	private String startArea;

	/**
	 * 末端
	 */
	private String endArea;

	/**
	 * 成员个数
	 */
	private Integer mnum;
    
	
	/**
	 * 排序号
	 */
	private int sort;

	private int priNum;
    
	private double iareaTariff;//首端区域输电费
	
	private double jareaTariff;//末端区域输电费
	
	private double jproTariff;//首端省内输电费
	
	private double iproTariff;//末端省内输电费
	
	private String startDate;//更新时间
	
	private String endDate;//结束时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMpath() {
		return mpath;
	}

	public void setMpath(String mpath) {
		this.mpath = mpath;
	}

	public Integer getMorder() {
		return morder;
	}

	public void setMorder(Integer morder) {
		this.morder = morder;
	}

	public String getStartArea() {
		return startArea;
	}

	public void setStartArea(String startArea) {
		this.startArea = startArea;
	}

	public String getEndArea() {
		return endArea;
	}

	public void setEndArea(String endArea) {
		this.endArea = endArea;
	}

	public Integer getMnum() {
		return mnum;
	}

	public void setMnum(Integer mnum) {
		this.mnum = mnum;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getPriNum() {
		return priNum;
	}

	public void setPriNum(int priNum) {
		this.priNum = priNum;
	}

	public double getIareaTariff() {
		return iareaTariff;
	}

	public void setIareaTariff(double iareaTariff) {
		this.iareaTariff = iareaTariff;
	}

	public double getJareaTariff() {
		return jareaTariff;
	}

	public void setJareaTariff(double jareaTariff) {
		this.jareaTariff = jareaTariff;
	}

	public double getJproTariff() {
		return jproTariff;
	}

	public void setJproTariff(double jproTariff) {
		this.jproTariff = jproTariff;
	}

	public double getIproTariff() {
		return iproTariff;
	}

	public void setIproTariff(double iproTariff) {
		this.iproTariff = iproTariff;
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

	/**
	 * 联络线
	 */
	private List<CorridorPathPo> corridorPaths;

	public List<CorridorPathPo> getCorridorPaths() {
		return corridorPaths;
	}

	public void setIntraCorridorPaths(List<CorridorPathPo> corridorPaths) {
		this.corridorPaths = corridorPaths;
	}
}
