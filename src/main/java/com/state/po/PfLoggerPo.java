package com.state.po;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月3日上午11:04:02
 */
@Alias("PfLoggerPo")
public class PfLoggerPo  extends BasePo{
	private static final long serialVersionUID = 1L;
	private String id;
	private String addName;
	private String addNameCn;
	private Date docCreated;
	private String ip;
	private String className;
	private String operName;
	private String operParams;
	private String operResult;
	private String resultMsg;
	
	private String operNameCn;
	private String modelName;
	
	private String createTime;
	
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOperNameCn() {
		return operNameCn;
	}
	public void setOperNameCn(String operNameCn) {
		this.operNameCn = operNameCn;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getOperParams() {
		return operParams;
	}
	public void setOperParams(String operParams) {
		this.operParams = operParams;
	}
	public String getOperResult() {
		return operResult;
	}
	public void setOperResult(String operResult) {
		this.operResult = operResult;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
}
