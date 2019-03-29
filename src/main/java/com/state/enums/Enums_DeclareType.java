package com.state.enums;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月12日下午9:09:03
 */
public enum Enums_DeclareType {
	ALLDAY("1a"), // 全天
	HIGHT("2a"), // 高峰
	LOW("3a");// 低谷
	private String value;

	public static String BUY = "buy";
	public static String SALE = "sale";
	
	/**
	 * 买卖类型
	 */
	public static String DROLE_SEND = "送电侧";
	public static String DROLE_RECV = "受电侧";
	
	/**
	 * 成交数据类型
	 */
	public static String DATATYPE_POWER = "电力";
	public static String DATATYPE_PRICE = "电价";
	/**
	 * 交易功率
	 */
	public static String DATATYPE_TRADING_POWER = "交易功率";
	
	private Enums_DeclareType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
