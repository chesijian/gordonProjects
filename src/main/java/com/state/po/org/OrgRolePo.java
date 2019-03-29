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
@Alias("OrgRolePo")
public class OrgRolePo extends BasePo {

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:24:00
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private String id;
	private int isSys;
	private Integer roleDesc;
	private String roleId;
	private String roleName;
	private String roleType;
	private String remark;
	private String addName;
	private String addNameCn;
	private String lastModifier;
	private String lastModifierCn;
	private Date docCreated;
	private Date lastModified;
	private Integer type;
	
	// Constructors

	/** default constructor */
	public OrgRolePo() {
	}

	/** minimal constructor */
	public OrgRolePo(String id) {
		this.id = id;
	}

	/** full constructor */
	public OrgRolePo(String id, int isSys, Integer roleDesc, String roleId, String roleName, String roleType, String remark, String addName, String addNameCn, String lastModifier, String lastModifierCn, Date docCreated, Date lastModified, Integer type) {
		this.id = id;
		this.isSys = isSys;
		this.roleDesc = roleDesc;
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.remark = remark;
		this.addName = addName;
		this.addNameCn = addNameCn;
		this.lastModifier = lastModifier;
		this.lastModifierCn = lastModifierCn;
		this.docCreated = docCreated;
		this.lastModified = lastModified;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIsSys() {
		return isSys;
	}

	public void setIsSys(int isSys) {
		this.isSys = isSys;
	}

	public Integer getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(Integer roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	


	// Property accessors
	
}