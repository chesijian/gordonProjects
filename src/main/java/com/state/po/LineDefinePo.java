package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 联络线定义表
 * 
 * @author jh
 *
 */
@SuppressWarnings("serial")
@Alias("LineDefinePo")
public class LineDefinePo extends BasePo {
	// 联络线名
	private String id;
	private String mcorhr;
	// 首端
	private String startArea;
	// 末端
	private String endArea;
	
	private String rate;
	/**
	 * 排序号
	 */
	private int sort;
// 	联络线输电费
	private Double tielinetariff;
	// 首端省份
	private String ipro;
	// 末端省份
	private String jpro;
	
	
	public Double getTielinetariff() {
		return tielinetariff;
	}

	public void setTielinetariff(Double tielinetariff) {
		this.tielinetariff = tielinetariff;
	}

	public String getIpro() {
		return ipro;
	}

	public void setIpro(String ipro) {
		this.ipro = ipro;
	}

	public String getJpro() {
		return jpro;
	}

	public void setJpro(String jpro) {
		this.jpro = jpro;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMcorhr() {
		return mcorhr;
	}

	public void setMcorhr(String mcorhr) {
		this.mcorhr = mcorhr;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	

}
