package com.state.po.org;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.state.po.BasePo;




/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月9日上午9:24:19
 */

public class OrgUserRolePo extends BasePo {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userUid;
	private String roleUid;
	private String roleId;
	private String userId;
	
	private String addName;
	private String addNameCn;
	private Date docCreated;

	// Constructors

	/** default constructor */
	public OrgUserRolePo() {
	}

	/** full constructor */
	public OrgUserRolePo( String roleId, String roleUid, String userId, String userUid, String addName, String addNameCn, Date docCreated) {
		this.roleId = roleId;
		this.roleUid = roleUid;
		this.userId = userId;
		this.userUid = userUid;
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

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getRoleUid() {
		return roleUid;
	}

	public void setRoleUid(String roleUid) {
		this.roleUid = roleUid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public Date getDocCreated() {
		return docCreated;
	}

	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
	}

	
	// Property accessors
	
	
}