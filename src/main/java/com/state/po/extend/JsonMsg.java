package com.state.po.extend;
/**
 * 用于增删改返回前台状态
 * @description
 * @author 大雄
 * @date 2016年8月9日下午5:12:52
 */
public class JsonMsg {
	//如果是0表示正常
	private boolean status = false;
	private String msg;
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
