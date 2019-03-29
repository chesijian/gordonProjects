package com.state.po.sys;

import java.util.Date;

import com.state.po.BasePo;


/**
 * @description
 * @author 大雄
 * @date 2016年8月3日下午3:37:07
 */
public class PfLoginCountPo  extends BasePo  {

	// Fields

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月3日下午3:35:14
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Date docCreated;
	private String ip;
	private Date lastModified;
	private Integer count;
	private Integer type;
	private String userId;

	// Constructors

	/** default constructor */
	public PfLoginCountPo() {
	}

	/** minimal constructor */
	public PfLoginCountPo(String id) {
		this.id = id;
	}

	/** full constructor */
	public PfLoginCountPo(String id,  Date docCreated, String ip, Date lastModified,  Integer count, Integer type, String userId) {
		this.id = id;
		this.docCreated = docCreated;
		this.ip = ip;
		this.lastModified = lastModified;
		this.count = count;
		this.type = type;
		this.userId = userId;
	}

	// Property accessors
	

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Date getDocCreated() {
		return this.docCreated;
	}

	public void setDocCreated(Date docCreated) {
		this.docCreated = docCreated;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}