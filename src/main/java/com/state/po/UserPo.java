package com.state.po;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 用户表
 */
@SuppressWarnings("serial")
@Alias("UserPo")
public class UserPo extends BasePo {
	
	private String id;
	
	private String userId;
	
	/**
	 * 用户名
	 */
	private String mname;

	/**
	 * 地区
	 */
	private String area;

	/**
	 * 密码
	 */
	private String mkey;

	/**
	 * 属性
	 */
	private String dtype;

	/**
	 * 备注
	 */
	private String descr;

	/**
	 * 登录时间
	 */
	private String intime;

	/**
	 * 登出时间
	 */
	private String outtime;
	
	/**
	 * 登出时间
	 */
	private String drole;
	
	/**
	 * 是否允许登录
	 */
	private String islogin;
	
	/**
	 * 交易日期
	 */
	private String matchDate;
	
	private String role;
	
	
	private String addName;
	private String addNameCn;
	private String lastModifier;
	private String lastModifierCn;
	private Date docCreated;
	private Date lastModified;
	private String autoGraph;//签名
	

	
	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	//************extrea*************//
	private String roleName;
	
	private String depart;
	private String departName;
	
	
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMkey() {
		return mkey;
	}

	public void setMkey(String mkey) {
		this.mkey = mkey;
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

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getOuttime() {
		return outtime;
	}

	public void setOuttime(String outtime) {
		this.outtime = outtime;
	}

	public String getIslogin() {
		return islogin;
	}

	public void setIslogin(String islogin) {
		this.islogin = islogin;
	}

	public String getDrole() {
		return drole;
	}

	public void setDrole(String drole) {
		this.drole = drole;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	public String getAddNameCn() {
		return addNameCn;
	}

	public void setAddNameCn(String addNameCn) {
		this.addNameCn = addNameCn;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getLastModifierCn() {
		return lastModifierCn;
	}

	public void setLastModifierCn(String lastModifierCn) {
		this.lastModifierCn = lastModifierCn;
	}

	public Date getDocCreated() {
		return docCreated;
	}

	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getAutoGraph() {
		return autoGraph;
	}

	public void setAutoGraph(String autoGraph) {
		this.autoGraph = autoGraph;
	}

}
