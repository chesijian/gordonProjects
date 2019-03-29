package com.state.po;

import org.apache.ibatis.type.Alias;

import com.state.po.BasePo;

@SuppressWarnings("serial")
@Alias("CorridorPathPo")
public class CorridorPathPo extends BasePo {
	private String id;
	private Integer pathPri; 
	private String mdirection;
	private String corhr1;
	private String corhr2;
	private String corhr3;
	private String corhr4;
	private String corhr5;
	private String corhr6;
	private String corhr7;
	private String corhr8;
	private String corhr9;
	private String corhr10;
	private Integer sort;
	private String pathUid;
	private float priceRatioA;
	private float priceRatioB;
	private float transLoss;
	private String data;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getPathPri() {
		return pathPri;
	}
	public void setPathPri(Integer pathPri) {
		this.pathPri = pathPri;
	}
	public String getMdirection() {
		return mdirection;
	}
	public void setMdirection(String mdirection) {
		this.mdirection = mdirection;
	}
	public String getCorhr1() {
		return corhr1;
	}
	public void setCorhr1(String corhr1) {
		this.corhr1 = corhr1;
	}
	public String getCorhr2() {
		return corhr2;
	}
	public void setCorhr2(String corhr2) {
		this.corhr2 = corhr2;
	}
	public String getCorhr3() {
		return corhr3;
	}
	public void setCorhr3(String corhr3) {
		this.corhr3 = corhr3;
	}
	public String getCorhr4() {
		return corhr4;
	}
	public void setCorhr4(String corhr4) {
		this.corhr4 = corhr4;
	}
	public String getCorhr5() {
		return corhr5;
	}
	public void setCorhr5(String corhr5) {
		this.corhr5 = corhr5;
	}
	public String getCorhr6() {
		return corhr6;
	}
	public void setCorhr6(String corhr6) {
		this.corhr6 = corhr6;
	}
	public String getCorhr7() {
		return corhr7;
	}
	public void setCorhr7(String corhr7) {
		this.corhr7 = corhr7;
	}
	public String getCorhr8() {
		return corhr8;
	}
	public void setCorhr8(String corhr8) {
		this.corhr8 = corhr8;
	}
	public String getCorhr9() {
		return corhr9;
	}
	public void setCorhr9(String corhr9) {
		this.corhr9 = corhr9;
	}
	public String getCorhr10() {
		return corhr10;
	}
	public void setCorhr10(String corhr10) {
		this.corhr10 = corhr10;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getPathUid() {
		return pathUid;
	}
	public void setPathUid(String pathUid) {
		this.pathUid = pathUid;
	}
	public float getPriceRatioA() {
		return priceRatioA;
	}
	public void setPriceRatioA(float priceRatioA) {
		this.priceRatioA = priceRatioA;
	}
	public float getPriceRatioB() {
		return priceRatioB;
	}
	public void setPriceRatioB(float priceRatioB) {
		this.priceRatioB = priceRatioB;
	}
	public float getTransLoss() {
		return transLoss;
	}
	public void setTransLoss(float transLoss) {
		this.transLoss = transLoss;
	}
	
	
	
 
}
