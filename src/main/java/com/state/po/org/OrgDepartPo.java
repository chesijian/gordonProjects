package com.state.po.org;




import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.state.po.BasePo;

/**
 * 角色
 * @description
 * @author 大雄
 * @date 2016年8月9日上午9:22:11
 */
@Alias("OrgDepartPo")
public class OrgDepartPo extends BasePo {

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:24:00
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private String id;
	private Integer sort;
	private String departId;
	private String departName;
	private String area;
	private String remark;
	private String addName;
	private String addNameCn;
	private String lastModifier;
	private String lastModifierCn;
	private Date docCreated;
	private Date lastModified;
	
	// Constructors

	/** default constructor */
	public OrgDepartPo() {
	}

	/** minimal constructor */
	public OrgDepartPo(String id) {
		this.id = id;
	}

	/** full constructor */
	public OrgDepartPo(String id,  Integer sort, String departId, String departName, String area,String remark, String addName, String addNameCn, String lastModifier, String lastModifierCn, Date docCreated, Date lastModified) {
		this.id = id;
		this.sort = sort;
		this.departId = departId;
		this.departName = departName;
		this.area = area;
		this.remark = remark;
		this.addName = addName;
		this.addNameCn = addNameCn;
		this.lastModifier = lastModifier;
		this.lastModifierCn = lastModifierCn;
		this.docCreated = docCreated;
		this.lastModified = lastModified;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	
	
}