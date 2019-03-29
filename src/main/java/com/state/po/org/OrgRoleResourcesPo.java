package com.state.po.org;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.state.po.BasePo;



/**
 * 角色资源
 * @description
 * @author 大雄
 * @date 2016年8月9日上午9:23:11
 */
@Alias("OrgRoleResourcesPo")
public class OrgRoleResourcesPo extends BasePo {

	// Fields

	
	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:23:05
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String roleUid;
	private String resourceUid;
	private String roleId;
	private String resourceId;
	private String type;
	private String belongUid;
	private String belongId;
	private String addName;
	private String addNameCn;
	private Date docCreated;

	// Constructors

	/** default constructor */
	public OrgRoleResourcesPo() {
	}

	/** full constructor */
	public OrgRoleResourcesPo(String roleUid, String resourceUid, String roleId, String resourceId, String type,  String belongUid, String belongId, String addName, String addNameCn, Date docCreated) {
		this.roleUid = roleUid;
		this.resourceUid = resourceUid;
		this.roleId = roleId;
		this.resourceId = resourceId;
		this.type = type;
		this.belongUid = belongUid;
		this.belongId = belongId;
		this.addName = addName;
		this.addNameCn = addNameCn;
		this.docCreated = docCreated;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleUid() {
		return roleUid;
	}

	public void setRoleUid(String roleUid) {
		this.roleUid = roleUid;
	}

	public String getResourceUid() {
		return resourceUid;
	}

	public void setResourceUid(String resourceUid) {
		this.resourceUid = resourceUid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBelongUid() {
		return belongUid;
	}

	public void setBelongUid(String belongUid) {
		this.belongUid = belongUid;
	}

	public String getBelongId() {
		return belongId;
	}

	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}

	public String getAddName() {
		return addName;
	}

	public Date getDocCreated() {
		return docCreated;
	}

	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
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

	

	// Property accessors
	
}