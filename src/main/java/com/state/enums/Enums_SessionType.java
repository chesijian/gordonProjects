package com.state.enums;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午4:25:10
 */
public enum Enums_SessionType {
	AREA("s_area"),//地区
	DCODE("s_dcode"),//代号
	USERINFO("s_userInfo"),//用户
	IS_MATCH("isMatch"),//是否计算的标记位
	ROLES("s_roles");//角色
	
	//RESOURCES("s_resources");
	private String value;

	//是否计算的标记位
	//public static String IS_MATCH = "isMatch";
	
	private Enums_SessionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
