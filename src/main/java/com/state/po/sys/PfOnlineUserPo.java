package com.state.po.sys;

import java.util.Date;

import com.state.po.BasePo;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月3日下午3:36:10
 */
public class PfOnlineUserPo extends BasePo {

	// Fields

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月3日下午3:37:07
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userUid;
	private String userId;
	private String userName;
	private String ip;
	private Integer type;
	private Date docCreated;
	private String createTime;
	/**
	 * sessionid
	 */
	private String seId;
	
	
	// Constructors

	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/** default constructor */
	public PfOnlineUserPo() {
	}

	/** minimal constructor */
	public PfOnlineUserPo(String id) {
		this.id = id;
	}

	/** full constructor */
	public PfOnlineUserPo(String id, String userUid,String userId, String userName, String ip, Integer type,  Date docCreated) {
		this.id = id;
		this.userUid = userUid;
		this.userId = userId;
		this.userName = userName;
		this.ip = ip;
		this.type = type;
		this.docCreated = docCreated;
	}

	public String getId() {
		return this.id;
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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
	public Date getDocCreated() {
		return this.docCreated;
	}

	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
	}

	

}