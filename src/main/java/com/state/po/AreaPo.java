package com.state.po;

import org.apache.ibatis.type.Alias;

/**
 * 地区表
 * 
 * @author jh
 *
 */
@Alias("AreaPo")
public class AreaPo extends BasePo {
	private static final long serialVersionUID = 1L;
	// 地区
	private String area;
	// 简称
	private String dcode;
	// 类型
	private String dtype;
	// 备注
	private String descr;
	// 区域
	private String region;
	//单子数量
	private Long count;
	//排序
	private int sort;
	//用于判断该地区有没有申报单
	private Integer isSheet;
	
	
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getIsSheet() {
		return isSheet;
	}

	public void setIsSheet(Integer isSheet) {
		this.isSheet = isSheet;
	}
}
